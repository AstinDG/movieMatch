package com.astindg.movieMatch.model;

import com.astindg.movieMatch.domain.MessageBuilder;
import com.astindg.movieMatch.domain.Session;

public enum Command {
    INITIAL {
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().initial(session).getKeyboards().initial().build();
        }
    },
    FRIENDS_MENU {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().selectOption().getKeyboards().friends().build();
        }
    },
    FRIENDS_LIST {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().friendList(session).getKeyboards().friends().build();
        }
    },
    FRIEND_ADD {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().inviteFriend(session).getButtons().inviteCode().build();
        }
    }, //special
    FRIEND_SELECT {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().selectFriend(session).getButtons().friendList(session, 0).build();
        }
    },
    FRIEND_REMOVE {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().friendRemove(session).getButtons().friendRemove(session).build();
        }
    },
    MOVIE_MENU {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().selectOption().getKeyboards().movie().build();
        }
    },
    MOVIE_MATCH {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            Language language = session.getUser().getLanguage();
            if (session.getCurrentFriend() == null) {
                Message message = Command.FRIEND_SELECT.getAnswer(session, messageBuilder);
                String errorText = messageBuilder.setLanguage(language).getText().friendNotSelectedError().build().getText();
                message.setText(errorText + "\n\n" + message.getText());

                return message;
            } else if (session.getLastRandomMovie() == null) {
                return messageBuilder.setLanguage(language).getText().moviesListEmpty().getKeyboards().movieMatch().build();
            } else {
                return messageBuilder.setLanguage(language).getRandomMovie(session).getKeyboards().movieMatch().build();
            }
        }
    }, //special
    MOVIE_LiKE {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            Language language = session.getUser().getLanguage();

            if (session.getHasNewMatches()) {
                return messageBuilder.setLanguage(language).getText().newMatch(session).getKeyboards().newMatch().build();
            } else if (session.getLastRandomMovie() != null) {
                return messageBuilder.setLanguage(language).getRandomMovie(session).getKeyboards().movieMatch().build();
            } else if (session.getMovieList() == null) {
                return messageBuilder.setLanguage(language).getText().movieMatchNotStarted().getKeyboards().initial().build();
            } else {
                return messageBuilder.setLanguage(language).getText().moviesListEmpty().getKeyboards().movieMatch().build();
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
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().selectOption().getKeyboards().movieLists().build();
        }
    },
    MOVIE_FAVORITES {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().favoritesMovies(session).getButtons().favoriteMovies(session, 0).build();
        }
    },
    MOVIE_DISLIKED {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().dislikedMovies(session).getButtons().dislikedMovies(session, 0).build();
        }
    },
    MOVIE_MATCHES {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            Language language = session.getUser().getLanguage();
            if(session.getCurrentFriend() == null){
                return messageBuilder.setLanguage(language)
                        .getText().friendNotSelectedError()
                        .getButtons().friendList(session, 0)
                        .build();
            }

            return messageBuilder.setLanguage(language)
                    .getText().movieMatchesWithFriend(session)
                    .getKeyboards().movie()
                    .build();
        }
    },
    RETURN_MAIN_MENU {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().selectOption().getKeyboards().initial().build();
        }
    },
    SETTINGS {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().selectOption().getKeyboards().settings().build();
        }
    },
    LANGUAGE {
        @Override
        public Message getAnswer(Session session, MessageBuilder messageBuilder) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().selectLanguage().getButtons().selectLanguage().build();
        }

    };

    public abstract Message getAnswer(Session session, MessageBuilder messageBuilder);
}
