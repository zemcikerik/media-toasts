#include "media_handler.hpp"

using namespace media_handler::windows;

windows_media_handler::windows_media_handler(const media_info_callback& on_info_callback)
    : on_media_info_callback_(on_info_callback), session_manager_(media_session_manager::RequestAsync().get())
{
    using sessions_changed = winrt::Windows::Media::Control::SessionsChangedEventArgs;

    sessions_changed_token_ = session_manager_.SessionsChanged([this](const media_session_manager&, const sessions_changed&)
    {
        unsubscribe_session_entries();
        subscribe_to_all_sessions();
    });

    subscribe_to_all_sessions();
}


windows_media_handler::~windows_media_handler()
{
    session_manager_.SessionsChanged(sessions_changed_token_);
    unsubscribe_session_entries();
}


void windows_media_handler::unsubscribe_session_entries()
{
    for (const auto& [session, token, last_info] : session_entries_)
    {
        session.MediaPropertiesChanged(token);
        delete last_info;
    }

    session_entries_.clear();
}


void windows_media_handler::subscribe_to_all_sessions()
{
    for (const auto& target_session : session_manager_.GetSessions())
    {
        using properties_changed = winrt::Windows::Media::Control::MediaPropertiesChangedEventArgs;

        auto last_info = new media_info {
            .artist = "",
            .title = "",
        };

        const auto token = target_session.MediaPropertiesChanged([this, last_info](const media_session& session, const properties_changed&)
        {
            const auto media_properties = session.TryGetMediaPropertiesAsync().get();

            const media_info info = {
                .artist = to_string(media_properties.Artist()),
                .title = to_string(media_properties.Title()),
            };

            if (info.title.length() == 0)
                return;

            if (info.title == last_info->title && info.artist == last_info->artist)
                return;

            *last_info = info;
            on_media_info_callback_(info);
        });
        
        session_entries_.push_back({
            .session = target_session,
            .token = token,
            .last_info = last_info,
        });
    }
}
