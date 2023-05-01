CREATE TABLE IF NOT EXISTS mpa_ratings
(
    mpa_rating_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        	VARCHAR(20) NOT NULL UNIQUE,
    description     VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name       VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films
(
    film_id          INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name             VARCHAR(255)     NOT NULL,
    description      TEXT,
    release_date     DATE             NOT NULL CHECK (release_date > '1895-12-28'),
    duration         INTEGER          NOT NULL CHECK (duration > 0),
    mpa_rating_id    INTEGER          REFERENCES mpa_ratings (mpa_rating_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id  INTEGER NOT NULL REFERENCES films (film_id) ON DELETE CASCADE,
    genre_id INTEGER NOT NULL REFERENCES genres (genre_id) ON DELETE RESTRICT,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  INTEGER generated by default as identity primary key,
    email    VARCHAR(255) NOT NULL,
    login    VARCHAR(255) NOT NULL UNIQUE,
    name     VARCHAR(255),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS friendships
(
    from_user_id      INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    to_user_id        INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
	is_confirmed      BOOLEAN NOT NULL DEFAULT FALSE,
	PRIMARY KEY (from_user_id, to_user_id)
);

CREATE TABLE IF NOT EXISTS film_likes
(
    film_id  INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    user_id  INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id)
);
