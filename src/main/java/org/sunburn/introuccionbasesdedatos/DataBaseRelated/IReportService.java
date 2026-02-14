package org.sunburn.introuccionbasesdedatos.DataBaseRelated;

import java.sql.SQLException;

public interface IReportService {
    String generateReport() throws SQLException;
}
