package talentshow.persistance;

import talentshow.entities.Juriu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JuriuRepository {
    private JdbcUtils dbUtils;

    public JuriuRepository( JdbcUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    public List<Juriu> getAll() {
        List<Juriu> rez = new ArrayList<>();
        try(Connection con = dbUtils.getConnection(); PreparedStatement preStmt = con.prepareStatement("SELECT * From juriu")){
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()){
                    int id=result.getInt(1);
                    String nume = result.getString(2);
                    String username = result.getString(3);
                    String password = result.getString(4);
                    Juriu copil = new Juriu(id, nume, username, password);
                    rez.add(copil);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getAll Juriu" + e.getMessage());
        }
        return rez;
    }
}
