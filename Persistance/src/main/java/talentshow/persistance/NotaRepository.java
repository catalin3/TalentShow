package talentshow.persistance;





import talentshow.entities.Nota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotaRepository {
    private JdbcUtils dbUtils;

    public NotaRepository(JdbcUtils dbUtils){
        this.dbUtils = dbUtils;
    }

    public void adaugaNota(Nota entity){
        try(Connection con = dbUtils.getConnection(); PreparedStatement preStmt = con.prepareStatement("INSERT INTO note VALUES (?,?,?)")){
            preStmt.setInt(1,entity.getIdJuriu());
            preStmt.setInt(2,entity.getIdParticipant());
            preStmt.setInt(3,entity.getNota());
            int result = preStmt.executeUpdate();
            modifyStatus(entity.getIdParticipant());
        } catch (SQLException e) {
            System.out.println("Error save Nota " + e.getMessage());
        }
    }

    private void modifyStatus(int idParticipant) {
        String status = "";
        try (Connection con = dbUtils.getConnection(); PreparedStatement preStmt = con.prepareStatement("SELECT  COUNT(*) FROM note WHERE idParticipant=?")) {
            preStmt.setInt(1, idParticipant);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    if (result.getInt(1) < 3) {
                        status = "PENDING";
                    } else if (result.getInt(1) == 3) {
                        status = calculeazaNota(idParticipant);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error modifyStatus NotaRepo " + e.getMessage());
        }

        //change status
        try (Connection con = dbUtils.getConnection(); PreparedStatement p = con.prepareStatement("UPDATE participanti SET status=? WHERE idParticipant=?")) {
            p.setString(1, status);
            p.setInt(2, idParticipant);
            int result = p.executeUpdate();
        } catch (SQLException e1) {
            System.out.println("Error modifyStatus NotaRepo 2" + e1.getMessage());
            e1.printStackTrace();
        }
    }

    private String calculeazaNota(int idParticipant) {
        try (Connection con = dbUtils.getConnection(); PreparedStatement preStmt = con.prepareStatement("SELECT SUM(nota) FROM note WHERE idParticipant=?")) {
            preStmt.setInt(1, idParticipant);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int sumaNota = result.getInt(1);
                    return String.valueOf(sumaNota);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Nota> getParticipanti(int idO) {
        List<Nota> rez = new ArrayList<>();
        try (Connection con = dbUtils.getConnection(); PreparedStatement preStmt = con.prepareStatement("SELECT * FROM note WHERE id_organizator=?")) {
            preStmt.setInt(1, idO);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int idP = result.getInt(2);
                    int nota = result.getInt(3);
                    rez.add(new Nota(idO, idP, nota));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rez;
    }


    public boolean verificaNota(int idO, int idP) {
        try(Connection con = dbUtils.getConnection(); PreparedStatement preparedStatement = con.prepareStatement("SELECT COUNT(*) FROM note WHERE idJuriu=? and idParticipant=?")){
            preparedStatement.setInt(1, idO);
            preparedStatement.setInt(2, idP);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    if(resultSet.getInt(1) != 0) {
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
