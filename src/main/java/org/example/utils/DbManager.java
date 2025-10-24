package org.example.utils;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.example.Main;
import org.example.model.Competition;
import org.example.model.Person;
import org.example.model.Dog;

public class DbManager {
    
    public static List<Competition> getCompetitions() throws SQLException {
        List<Competition> competitions = new ArrayList<>();
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT (id, place, dog_id, judge_id, dog.name, dog.breed, owner.name, owner.surname, owner.middlename, judge.name, judge.surname, judge.middlename) FROM competitions INNER JOIN dogs ON competitions.dog_id = dogs.id JOIN owners ON dogs.owner_id = owners.id JOIN judges ON competitions.judge_id = judges.id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                competitions.add(new Competition(resultSet.getInt("id"), resultSet.getInt("place"), resultSet.getInt("dog_id"), resultSet.getInt("judge_id"), resultSet.getString("dog.name"), resultSet.getString("dog.breed"), resultSet.getString("owner.name") + " " + resultSet.getString("owner.surname") + " " + resultSet.getString("owner.middlename"), resultSet.getString("judge.name") + " " + resultSet.getString("judge.surname") + " " + resultSet.getString("judge.middlename")));
            }
            return competitions;
        }
    }

    public static List<Person> getJudges() throws SQLException {
        List<Person> judges = new ArrayList<>();
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT * FROM judges";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                judges.add(new Person(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("middlename"), "judge"));
            }
            return judges;
        }
    }

    public static List<Person> getOwners() throws SQLException {
        List<Person> owners = new ArrayList<>();
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT * FROM owners";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                owners.add(new Person(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("middlename"), "owner"));
            }
            return owners;
        }
    }

    public static List<Dog> getDogs() throws SQLException {
        List<Dog> dogs = new ArrayList<>();
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT id, name, breed, owner.id, owner.name, owner.surname, owner.middlename FROM dogs INNER JOIN owners ON dogs.owner_id = owners.id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                dogs.add(new Dog(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("breed"), new Person(resultSet.getInt("owner.id"), resultSet.getString("owner.name"), resultSet.getString("owner.surname"), resultSet.getString("owner.middlename"), "owner")));
            }
            return dogs;
        }
    }

    public static Person getOwner(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT * FROM owners WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Person(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("middlename"), "owner");
            }
            return null;
        }
    }

    public static Person getJudge(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT * FROM judges WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Person(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("middlename"), "judge");
            }
            return null;
        }
    }

    public static Dog getDog(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT id, name, breed, owner.id, owner.name, owner.surname, owner.middlename FROM dogs INNER JOIN owners ON dogs.owner_id = owners.id WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Dog(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("breed"), new Person(resultSet.getInt("owner.id"), resultSet.getString("owner.name"), resultSet.getString("owner.surname"), resultSet.getString("owner.middlename"), "owner"));
            }
            return null;
        }
    }

    public static void addDog(Dog dog) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "INSERT INTO dogs (name, breed, owner_id) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, dog.getName());
            statement.setString(2, dog.getBreed());
            statement.setInt(3, dog.getOwner().getId());
            statement.executeUpdate();
        }
    }

    public static void addOwner(Person owner) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "INSERT INTO owners (name, surname, middlename) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, owner.getName());
            statement.setString(2, owner.getSurname());
            statement.setString(3, owner.getMiddlename());
            statement.executeUpdate();
        }
    }

    public static void addJudge(Person judge) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "INSERT INTO judges (name, surname, middlename) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, judge.getName());
            statement.setString(2, judge.getSurname());
            statement.setString(3, judge.getMiddlename());
            statement.executeUpdate();
        }
    }

    public static void addCompetition(Competition competition) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "INSERT INTO competitions (place, dog_id, judge_id) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, competition.getPlace());
            statement.setInt(2, competition.getDogId());
            statement.setInt(3, competition.getJudgeId());
            statement.executeUpdate();
        }
    }

    public static void updateDog(Dog dog) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "UPDATE dogs SET name = ?, breed = ?, owner_id = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, dog.getName());
            statement.setString(2, dog.getBreed());
            statement.setInt(3, dog.getOwner().getId());
            statement.setInt(4, dog.getId());
            statement.executeUpdate();
        }
    }

    public static void updateOwner(Person owner) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "UPDATE owners SET name = ?, surname = ?, middlename = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, owner.getName());
            statement.setString(2, owner.getSurname());
            statement.setString(3, owner.getMiddlename());
            statement.setInt(4, owner.getId());
            statement.executeUpdate();
        }
    }

    public static void updateJudge(Person judge) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "UPDATE judges SET name = ?, surname = ?, middlename = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, judge.getName());
            statement.setString(2, judge.getSurname());
            statement.setString(3, judge.getMiddlename());
            statement.setInt(4, judge.getId());
            statement.executeUpdate();
        }
    }

    public static void updateCompetition(Competition competition) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "UPDATE competitions SET place = ?, dog_id = ?, judge_id = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, competition.getPlace());
            statement.setInt(2, competition.getDogId());
            statement.setInt(3, competition.getJudgeId());
            statement.setInt(4, competition.getId());
            statement.executeUpdate();
        }
    }

    public static void deleteDog(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "DELETE FROM dogs WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Dog not found");
            }
        }
    }   

    public static void deleteOwner(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "DELETE FROM owners WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Owner not found");
            }
        }
    }

    public static void deleteJudge(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "DELETE FROM judges WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Judge not found");
            }
        }
    }

    public static void deleteCompetition(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "DELETE FROM competitions WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Competition not found");
            }
        }
    }
}
