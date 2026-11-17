package com.theatre.model;

/**
 * Movie domain class (FR-01: Manage movie details).
 */
public class Movie {
    private int movieId;
    private String title;
    private String genre;
    private String language;
    private int durationMinutes;
    private String releaseDate;

    public Movie(int movieId, String title, String genre, String language, int durationMinutes, String releaseDate) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be strictly positive!");
        }
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.durationMinutes = durationMinutes;
        this.releaseDate = releaseDate;
    }

    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getLanguage() { return language; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getReleaseDate() { return releaseDate; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s, %s) - %d mins [Released: %s]",
                movieId, title, genre, language, durationMinutes, releaseDate);
    }
}
