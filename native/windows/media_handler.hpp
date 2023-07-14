#pragma once
#include <functional>
#include <vector>
#include <winrt/base.h>
#include <winrt/Windows.Foundation.h>
#include <winrt/Windows.Foundation.Collections.h>
#include <winrt/Windows.Media.Control.h>
#include "media_info.hpp"

namespace media_handler::windows
{
    using event_token = winrt::event_token;
    using media_session = winrt::Windows::Media::Control::GlobalSystemMediaTransportControlsSession;
    using media_session_manager = winrt::Windows::Media::Control::GlobalSystemMediaTransportControlsSessionManager;

    using media_info_callback = std::function<void(const media_info&)>;

    struct media_session_entry
    {
        const media_session session;
        const event_token token;
        media_info* last_info;
    };

    class windows_media_handler
    {
    public:
        explicit windows_media_handler(const media_info_callback&);
        ~windows_media_handler();

    private:
        const media_info_callback on_media_info_callback_;
        const media_session_manager session_manager_;

        event_token sessions_changed_token_;
        std::vector<media_session_entry> session_entries_ = {};

        void unsubscribe_session_entries();
        void subscribe_to_all_sessions();
    };
}
