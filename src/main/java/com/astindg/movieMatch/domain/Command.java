package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Language;
import com.astindg.movieMatch.model.Message;

public enum Command {
    INITIAL {
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withInitialTemplate(session).keyboards().initial().build();
        }
    },
    FRIENDS_MENU {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withSelectOptionText().keyboards().friends().build();
        }
    },
    FRIENDS_LIST {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withFriendListText(session).keyboards().friends().build();
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
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withSelectFriendText(session).withFriendListButtons(session, 0).build();
        }
    },
    FRIEND_REMOVE {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withFriendRemoveText(session).withFriendRemoveButtons(session).build();
        }
    },
    MOVIE_MENU {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withSelectOptionText().keyboards().movie().build();
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
                return messageBuilder.setLanguage(language).withNoMoviesText().keyboards().movieMatch().build();
            } else {
                return messageBuilder.setLanguage(language).withRandomMovie(session).keyboards().movieMatch().build();
            }
        }
    }, //special
    MOVIE_LiKE {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            Language language = session.getUser().getLanguage();

            if (session.getHasNewMatches()) {
                return messageBuilder.setLanguage(language).withNewMatchMovieMessage(session).keyboards().newMatch().build();
            } else if (session.getLastMovieShown() != null) {
                return messageBuilder.setLanguage(language).withRandomMovie(session).keyboards().movieMatch().build();
            } else if (session.getMovieList() == null) {
                return messageBuilder.setLanguage(language).withMovieMatchNotStarted().keyboards().initial().build();
            } else {
                return messageBuilder.setLanguage(language).withNoMoviesText().keyboards().movieMatch().build();
            }
        }
    }, //special
    MOVIE_DISLIKE {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return MOVIE_LiKE.getAnswer(session, messageBuilder);
        }
    }, //special
    MOVIE_LIST {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withSelectOptionText().keyboards().movieLists().build();
        }
    },
    MOVIE_FAVORITES {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withFavoritesMoviesText(session).withFavoriteMoviesButtons(session, 0).build();
        }
    },
    MOVIE_DISLIKED {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withDislikedMoviesText(session).withDislikedMoviesButtons(session, 0).build();
        }
    },
    MOVIE_MATCHES {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            Language language = session.getUser().getLanguage();
            if(session.getCurrentFriend() == null){
                return messageBuilder.setLanguage(language)
                        .withFriendNotSelectedError()
                        .withFriendListButtons(session, 0)
                        .build();
            }

            return messageBuilder.setLanguage(language)
                    .withMovieMatchesWithFriend(session)
                    .keyboards().movie()
                    .build();
        }
    },
    RETURN_MAIN_MENU {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withSelectOptionText().keyboards().initial().build();
        }
    },
    SETTINGS {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withSelectOptionText().keyboards().settings().build();
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
