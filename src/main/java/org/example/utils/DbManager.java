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

/**
 * Класс для управления операциями с базой данных.
 * Предоставляет методы для выполнения CRUD операций
 */
public class DbManager {
    
    /**
     * Получает список всех соревнований из базы данных.
     *
     * @return список всех соревнований
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static List<Competition> getCompetitions() throws SQLException {
        List<Competition> competitions = new ArrayList<>();
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT competition.id, place, dog_id, judge_id, owner_id, dog.name, dog.breed, owner.name, owner.surname, owner.middlename, judges.name, judges.surname, judges.middlename FROM competition LEFT JOIN dog ON competition.dog_id = dog.id LEFT JOIN owner ON dog.owner_id = owner.id LEFT JOIN judges ON competition.judge_id = judges.id";
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                competitions.add(new Competition(resultSet.getInt("id"), resultSet.getInt("place"), resultSet.getInt("dog_id"), resultSet.getInt("judge_id"), resultSet.getInt("owner_id"), resultSet.getString("dog.name"), resultSet.getString("dog.breed"), resultSet.getString("owner.name") + " " + resultSet.getString("owner.surname") + " " + resultSet.getString("owner.middlename"), resultSet.getString("judges.name") + " " + resultSet.getString("judges.surname") + " " + resultSet.getString("judges.middlename")));
            }
            return competitions;
        }
    }

    /**
     * Получает список всех судей из базы данных.
     *
     * @return список всех судей
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static List<Person> getJudges() throws SQLException {
        List<Person> judges = new ArrayList<>();
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT * FROM judges";
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                judges.add(new Person(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("middlename"), "judge"));
            }
            return judges;
        }
    }

    /**
     * Получает список всех владельцев собак из базы данных.
     *
     * @return список всех владельцев
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static List<Person> getOwners() throws SQLException {
        List<Person> owners = new ArrayList<>();
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT * FROM owner";
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                owners.add(new Person(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("middlename"), "owner"));
            }
            return owners;
        }
    }

    /**
     * Получает список всех собак из базы данных.
     *
     * @return список всех собак
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static List<Dog> getDogs() throws SQLException {
        List<Dog> dogs = new ArrayList<>();
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT dog.id, dog.name, breed, owner.id, owner.name, owner.surname, owner.middlename FROM dog INNER JOIN owner ON dog.owner_id = owner.id";
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                dogs.add(new Dog(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("breed"), new Person(resultSet.getInt("owner.id"), resultSet.getString("owner.name"), resultSet.getString("owner.surname"), resultSet.getString("owner.middlename"), "owner")));
            }
            return dogs;
        }
    }

    /**
     * Получает информацию о владельце собаки по его идентификатору.
     *
     * @param id идентификатор владельца
     * @return объект Person с информацией о владельце или null, если владелец не найден
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static Person getOwner(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT * FROM owner WHERE id = ?";
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Person(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("middlename"), "owner");
            }
            return null;
        }
    }

    /**
     * Получает информацию о судье по его идентификатору.
     *
     * @param id идентификатор судьи
     * @return объект Person с информацией о судье или null, если судья не найден
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static Person getJudge(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT * FROM judges WHERE id = ?";
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Person(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("middlename"), "judge");
            }
            return null;
        }
    }

    /**
     * Получает информацию о собаке по её идентификатору.
     *
     * @param id идентификатор собаки
     * @return объект Dog с информацией о собаке или null, если собака не найдена
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static Dog getDog(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT dog.id, dog.name, breed, owner.id, owner.name, owner.surname, owner.middlename FROM dog INNER JOIN owner ON dog.owner_id = owner.id WHERE dog.id = ?";
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Dog(resultSet.getInt("dog.id"), resultSet.getString("dog.name"), resultSet.getString("breed"), new Person(resultSet.getInt("owner.id"), resultSet.getString("owner.name"), resultSet.getString("owner.surname"), resultSet.getString("owner.middlename"), "owner"));
            }
            return null;
        }
    }

    /**
     * Добавляет новую собаку в базу данных.
     *
     * @param dog объект Dog с информацией о собаке
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static void addDog(Dog dog) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "INSERT INTO dog (name, breed, owner_id) VALUES (?, ?, ?)";
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, dog.getName());
            statement.setString(2, dog.getBreed());
            statement.setInt(3, dog.getOwner().getId());
            statement.executeUpdate();
        }
    }

    /**
     * Добавляет нового владельца собаки в базу данных.
     *
     * @param owner объект Person с информацией о владельце
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static void addOwner(Person owner) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "INSERT INTO owner (name, surname, middlename) VALUES (?, ?, ?)";
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, owner.getName());
            statement.setString(2, owner.getSurname());
            statement.setString(3, owner.getMiddlename());
            statement.executeUpdate();
        }
    }

    /**
     * Добавляет нового судью в базу данных.
     *
     * @param judge объект Person с информацией о судье
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static void addJudge(Person judge) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "INSERT INTO judges (name, surname, middlename) VALUES (?, ?, ?)";
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, judge.getName());
            statement.setString(2, judge.getSurname());
            statement.setString(3, judge.getMiddlename());
            statement.executeUpdate();
        }
    }

    /**
     * Добавляет новое соревнование в базу данных.
     *
     * @param competition объект Competition с информацией о соревновании
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static void addCompetition(Competition competition) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "INSERT INTO competition (place, dog_id, judge_id) VALUES (?, ?, ?)";
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, competition.getPlace());
            statement.setInt(2, competition.getDogId());
            statement.setInt(3, competition.getJudgeId());
            statement.executeUpdate();
        }
    }

    /**
     * Обновляет информацию о собаке в базе данных.
     *
     * @param dog объект Dog с обновленной информацией (должен содержать id собаки)
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static void updateDog(Dog dog) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "UPDATE dog SET name = ?, breed = ?, owner_id = ? WHERE id = ?";
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, dog.getName());
            statement.setString(2, dog.getBreed());
            statement.setInt(3, dog.getOwner().getId());
            statement.setInt(4, dog.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Обновляет информацию о владельце собаки в базе данных.
     *
     * @param owner объект Person с обновленной информацией (должен содержать id владельца)
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static void updateOwner(Person owner) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "UPDATE owner SET name = ?, surname = ?, middlename = ? WHERE id = ?";
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, owner.getName());
            statement.setString(2, owner.getSurname());
            statement.setString(3, owner.getMiddlename());
            statement.setInt(4, owner.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Обновляет информацию о судье в базе данных.
     *
     * @param judge объект Person с обновленной информацией (должен содержать id судьи)
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static void updateJudge(Person judge) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "UPDATE judges SET name = ?, surname = ?, middlename = ? WHERE id = ?";
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, judge.getName());
            statement.setString(2, judge.getSurname());
            statement.setString(3, judge.getMiddlename());
            statement.setInt(4, judge.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Обновляет информацию о соревновании в базе данных.
     *
     * @param competition объект Competition с обновленной информацией (должен содержать id соревнования)
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static void updateCompetition(Competition competition) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "UPDATE competition SET place = ?, dog_id = ?, judge_id = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, competition.getPlace());
            statement.setInt(2, competition.getDogId());
            statement.setInt(3, competition.getJudgeId());
            statement.setInt(4, competition.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Проверяет, есть ли соревнования для указанной собаки.
     *
     * @param dogId идентификатор собаки
     * @return true, если есть соревнования для данной собаки, иначе false
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static boolean haveCompetitionsForDog(int dogId) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT COUNT(*) FROM competition WHERE dog_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, dogId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("COUNT(*)") > 0;
        }
    }

    /**
     * Проверяет, есть ли соревнования с участием указанного судьи.
     *
     * @param judgeId идентификатор судьи
     * @return true, если есть соревнования с участием данного судьи, иначе false
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static boolean haveCompetitionsForJudge(int judgeId) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT COUNT(*) FROM competition WHERE judge_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, judgeId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("COUNT(*)") > 0;
        }
    }


    /**
     * Удаляет собаку из базы данных по её идентификатору.
     *
     * @param id идентификатор собаки
     * @throws SQLException если произошла ошибка при работе с базой данных или собака не найдена
     */
    public static void deleteDog(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "DELETE FROM dog WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Dog not found");
            }
        }
    }   

    /**
     * Проверяет, есть ли собаки у указанного владельца.
     *
     * @param ownerId идентификатор владельца
     * @return true, если у владельца есть собаки, иначе false
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static boolean haveDogsForOwner(int ownerId) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT COUNT(*) FROM dog WHERE owner_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, ownerId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("COUNT(*)") > 0;
        }
    }

    /**
     * Удаляет владельца собаки из базы данных по его идентификатору.
     *
     * @param id идентификатор владельца
     * @throws SQLException если произошла ошибка при работе с базой данных или владелец не найден
     */
    public static void deleteOwner(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "DELETE FROM owner WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Owner not found");
            }
        }
    }

    /**
     * Удаляет судью из базы данных по его идентификатору.
     *
     * @param id идентификатор судьи
     * @throws SQLException если произошла ошибка при работе с базой данных или судья не найден
     */
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

    /**
     * Удаляет соревнование из базы данных по его идентификатору.
     *
     * @param id идентификатор соревнования
     * @throws SQLException если произошла ошибка при работе с базой данных или соревнование не найдено
     */
    public static void deleteCompetition(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "DELETE FROM competition WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Competition not found");
            }
        }
    }

    /**
     * Получает информацию о соревновании по его идентификатору.
     *
     * @param id идентификатор соревнования
     * @return объект Competition с информацией о соревновании или null, если соревнование не найдено
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public static Competition getCompetitionWithId(int id) throws SQLException {
        try (Connection connection = Main.getConnection()) {
            String query = "SELECT * FROM competition LEFT JOIN dog ON competition.dog_id = dog.id LEFT JOIN owner ON dog.owner_id = owner.id LEFT JOIN judges ON competition.judge_id = judges.id WHERE competition.id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? new Competition(resultSet.getInt("id"), resultSet.getInt("place"), resultSet.getInt("dog_id"), resultSet.getInt("judge_id"), resultSet.getInt("owner_id"), resultSet.getString("dog.name"), resultSet.getString("dog.breed"), resultSet.getString("owner.name") + " " + resultSet.getString("owner.surname") + " " + resultSet.getString("owner.middlename"), resultSet.getString("judges.name") + " " + resultSet.getString("judges.surname") + " " + resultSet.getString("judges.middlename")) : null;
        }
    }
    
}
