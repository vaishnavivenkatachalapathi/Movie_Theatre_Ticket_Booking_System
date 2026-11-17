package com.theatre.dao;

import com.theatre.model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Movie entity (Unit 4: Database Connection).
 */
public class MovieDAO {

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT movie_id, title, genre, language, duration_minutes, release_date FROM movie ORDER BY release_date DESC";

        Connection conn = DBConnection.getConnection();
        if (conn == null) return movies;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("movie_id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("language"),
                        rs.getInt("duration_minutes"),
                        rs.getDate("release_date").toString()
                );
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.err.println("[DAO ERROR] Failed to fetch movies: " + e.getMessage());
        }
        return movies;
    }

    public boolean insertMovie(Movie movie) {
        String sql = "INSERT INTO movie (movie_id, title, genre, language, duration_minutes, release_date) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movie.getMovieId());
            pstmt.setString(2, movie.getTitle());
            pstmt.setString(3, movie.getGenre());
            pstmt.setString(4, movie.getLanguage());
            pstmt.setInt(5, movie.getDurationMinutes());
            pstmt.setDate(6, Date.valueOf(movie.getReleaseDate()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DAO ERROR] Insert movie failed: " + e.getMessage());
            return false;
        }
    }
}
