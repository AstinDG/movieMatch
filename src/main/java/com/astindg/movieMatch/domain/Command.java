package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Language;
import com.astindg.movieMatch.model.Message;

public enum Command {
    INITIAL {
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withInitialTemplate(session).withInitialKeyboard().build();
        }
    },
    FRIENDS_MENU {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withSelectOptionText().withFriendMenuKeyboard().build();
        }
    },
    FRIENDS_LIST {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withFriendListText(session).withFriendMenuKeyboard().build();
        }
    },
    FRIEND_ADD {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withInviteFriendTemplate(session).withEnterInviteCodeButton().build();
        }
    }, //special
    FRIEND_SELECT {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withSelectFriendText(session).withFriendListButtons(session).build();
        }
    },
    FRIEND_REMOVE {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withFriendRemoveText().withFriendRemoveButtons(session).build();
        }
    },
    MOVIE_MENU {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withSelectOptionText().withMovieMenuKeyboard().build();
        }
    },
    MOVIE_MATCH {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            Language language = session.getUser().getLanguage();
            if (session.getCurrentFriend() == null) {
                Message message = Command.FRIEND_SELECT.getAnswer(session, messageBuilder);
                String errorText = messageBuilder.setLanguage(language).withFriendNotSelectedError().build().getText();
                message.setText(errorText + "\n\n" + message.getText());

                return message;
            } else if (session.getMovieList() == null || session.getLastMovieShown() == null) {
                return messageBuilder.setLanguage(language).withNoMoviesText().withMovieMatchKeyboard().build();
            } else {
                return messageBuilder.setLanguage(language).withRandomMovie(session).withMovieMatchKeyboard().build();
            }
        }
    }, //special
    MOVIE_LiKE {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            Language language = session.getUser().getLanguage();

            if (session.getHasNewMatches()) {
                return messageBuilder.setLanguage(language).withNewMatchMovieMessage(session).withNewMatchKeyBoard().build();
            } else if (session.getLastMovieShown() != null) {
                return messageBuilder.setLanguage(language).withRandomMovie(session).withMovieMatchKeyboard().build();
            } else if (session.getMovieList() == null) {
                return messageBuilder.setLanguage(language).withMovieMatchNotStarted().withInitialKeyboard().build();
            } else {
                return messageBuilder.setLanguage(language).withNoMoviesText().withMovieMatchKeyboard().build();
            }
        }
    }, //special
    MOVIE_DISLIKE {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return MOVIE_LiKE.getAnswer(session, messageBuilder);
        }
    }, //special
    MOVIE_FAVORITES {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withFavoritesMoviesText(session).withFavoriteMoviesButtons(session).build();
        }
    },
    MOVIE_MATCHES {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            Language language = session.getUser().getLanguage();
            if(session.getCurrentFriend() == null){
                return messageBuilder.setLanguage(language)
                        .withFriendNotSelectedError()
                        .withFriendListButtons(session)
                        .build();
            }

            return messageBuilder.setLanguage(language)
                    .withMovieMatchesWithFriend(session)
                    .withMovieMenuKeyboard()
                    .build();
        }
    },
    RETURN_MAIN_MENU {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withSelectOptionText().withInitialKeyboard().build();
        }
    },
    SETTINGS {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withSelectOptionText().withSettingsKeyboard().build();
        }
    },
    LANGUAGE {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withSelectLanguageText().withSelectLanguageButtons().build();
        }

    };

    public abstract Message getAnswer(Session session, MessageBuilder messageBuilder);
}
