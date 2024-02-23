package com.astindg.movieMatch.model;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.EnumSet;

import static com.astindg.movieMatch.model.Language.*;

public enum Genre {

    ACTION{
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Action";
                case UA -> "Бойовик";
                case RU -> "Боевик";
            };
        }
    },
    BIOGRAPHY{
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Biography";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    WESTERN {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Western";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    WAR {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "War";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    DETECTIVE {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Detective";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    DOCUMENTARY {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Documentary";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    DRAMA {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Drama";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    HISTORY {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "History";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    COMEDY {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Comedy";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    CRIME {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Crime";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    MELODRAMA {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Melodrama";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    ANIMATION {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Animation";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    EDUCATIONAL {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Educational";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    ADVENTURE {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Adventure";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    ROMANCE {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Romance";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    FAMILY {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Family";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    THRILLER {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Thriller";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    HORROR {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Horror";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    SCIENCE_FICTION {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Sci-Fi";
                case UA -> "";
                case RU -> "";
            };
        }
    },
    FANTASY {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Fantasy";
                case UA -> "";
                case RU -> "";
            };
        }
    };

    public abstract String getName(Language language);
}
