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
                case UA -> "Біографія";
                case RU -> "";
            };
        }
    },
    WESTERN {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Western";
                case UA -> "Вестерн";
                case RU -> "Вестерн";
            };
        }
    },
    WAR {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "War";
                case UA -> "Військовий";
                case RU -> "Военный";
            };
        }
    },
    DETECTIVE {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Detective";
                case UA -> "Детектив";
                case RU -> "Детектив";
            };
        }
    },
    DOCUMENTARY {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Documentary";
                case UA -> "Документальний";
                case RU -> "Документальный";
            };
        }
    },
    DRAMA {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Drama";
                case UA -> "Драма";
                case RU -> "Драма";
            };
        }
    },
    HISTORY {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "History";
                case UA -> "Історичний";
                case RU -> "Исторический";
            };
        }
    },
    COMEDY {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Comedy";
                case UA -> "Комедія";
                case RU -> "Комедия";
            };
        }
    },
    CRIME {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Crime";
                case UA -> "Кримінальний";
                case RU -> "Криминальный";
            };
        }
    },
    MELODRAMA {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Melodrama";
                case UA -> "Мелодрама";
                case RU -> "Мелодрама";
            };
        }
    },
    ANIMATION {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Animation";
                case UA -> "Мультфільм";
                case RU -> "Мультфильм";
            };
        }
    },
    EDUCATIONAL {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Educational";
                case UA -> "Навчальний";
                case RU -> "Учебный";
            };
        }
    },
    ADVENTURE {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Adventure";
                case UA -> "Пригоди";
                case RU -> "Приключения";
            };
        }
    },
    ROMANCE {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Romance";
                case UA -> "Романтичний";
                case RU -> "Романтический";
            };
        }
    },
    FAMILY {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Family";
                case UA -> "Сімейний";
                case RU -> "Семейный";
            };
        }
    },
    THRILLER {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Thriller";
                case UA -> "Трилер";
                case RU -> "Триллер ";
            };
        }
    },
    HORROR {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Horror";
                case UA -> "Жахи";
                case RU -> "Ужасы";
            };
        }
    },
    SCIENCE_FICTION {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Sci-Fi";
                case UA -> "Наукова фантастика";
                case RU -> "Научная фантастика";
            };
        }
    },
    FANTASY {
        @Override
        public String getName(Language language) {
            return switch (language) {
                case EN -> "Fantasy";
                case UA -> "Фентазі";
                case RU -> "Фэнтази";
            };
        }
    };

    public abstract String getName(Language language);
}
