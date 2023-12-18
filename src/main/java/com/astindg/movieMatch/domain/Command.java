package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Message;

public enum Command {
    INITIAL {
        public Message getAnswer(Session session) {
            return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withInitialTemplate(session).withInitialKeyboard().build();
        }
    },
    FRIENDS_MENU {
        @Override
        public Message getAnswer(Session session) {
            return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withSelectOptionText().withFriendMenuKeyboard().build();
        }
    },
    FRIENDS_LIST {
        @Override
        public Message getAnswer(Session session) {
            return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withFriendListText(session).withFriendMenuKeyboard().build();
        }
    },
    FRIEND_ADD {
        @Override
        public Message getAnswer(Session session) {
            return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withInviteFriendTemplate(session).withEnterInviteCodeButton().build();
        }
    }, //special
    FRIEND_SELECT {
        @Override
        public Message getAnswer(Session session) {
            return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withSelectFriendText().withFriendListButtons(session).build();
        }
    },
    FRIEND_REMOVE {
        @Override
        public Message getAnswer(Session session) {
            return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withFriendRemoveText().withFriendRemoveButtons(session).build();
        }
    },
    MOVIE_MENU {
        @Override
        public Message getAnswer(Session session) {
            return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withSelectOptionText().withMovieMenuKeyBoard().build();
        }
    },
    MOVIE_MATCH {
        @Override
        public Message getAnswer(Session session) {
            if (session.getCurrentFriend() == null) {
                return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withFriendNotSelectedError().withInitialKeyboard().build();
            } else if (session.getMovieList() == null || session.getLastMovieShown() == null) {
                return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withNoMoviesText().withMovieMatchKeyboard().build();
            } else {
                return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withRandomMovie(session).withMovieMatchKeyboard().build();
            }
        }
    }, //special
    MOVIE_LiKE {
        @Override
        public Message getAnswer(Session session) {
            if (session.getLastMovieShown() != null) {
                return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withRandomMovie(session).withMovieMatchKeyboard().build();
            } else if (session.getMovieList() == null) {
                return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withText("No films have been shown to you").withInitialKeyboard().build();
            } else {
                return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withText("No new movies for you yet :(").withMovieMatchKeyboard().build();
            }
        }
    }, //special
    MOVIE_DISLIKE {
        @Override
        public Message getAnswer(Session session) {
            return MOVIE_LiKE.getAnswer(session);
        }
    }, //special
    MOVIE_FAVORITES {
        @Override
        public Message getAnswer(Session session) {
            return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withFavoritesMoviesText(session).withMovieMenuKeyBoard().build();
        }
    },
    RETURN_MAIN_MENU {
        @Override
        public Message getAnswer(Session session) {
            return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withSelectOptionText().withInitialKeyboard().build();
        }
    },

    SETTINGS{
        @Override
        public Message getAnswer(Session session){
            return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withSelectOptionText().withSettingsKeyboard().build();
        }
    },
    LANGUAGE{
        @Override
        public Message getAnswer(Session session){
            return MESSAGE_BUILDER.setLanguage(session.getUser().getLanguage()).withSelectLanguageText().withSelectLanguageButtons().build();
        }

    };

    private static final MessageBuilder MESSAGE_BUILDER = new MessageBuilder();

    public abstract Message getAnswer(Session session);
}
