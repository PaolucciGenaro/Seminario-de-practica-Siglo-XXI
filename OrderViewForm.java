import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class OrderViewForm extends JFrame {
    public OrderViewForm() {
        setTitle("Ver Pedidos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el JTable para mostrar los pedidos
        JTable orderTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(orderTable);

        // Cargar los datos de los pedidos desde la base de datos
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_inventario", "root", "rosario98");
             Statement stmt = conn.createStatement()) {

            String query = "SELECT p.id_pedido, p.fecha_pedido, p.total, c.nombre " +
                           "FROM pedidos p " +
                           "JOIN clientes c ON p.id_cliente = c.id_cliente";
            ResultSet rs = stmt.executeQuery(query);

            // Construir la tabla con los datos del ResultSet
            DefaultTableModel model = buildTableModel(rs);
            orderTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        // Obtener los metadatos de la base de datos
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Construir las columnas de la tabla
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Construir las filas de la tabla
        java.util.List<Object[]> rows = new java.util.ArrayList<>();
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            rows.add(row);
        }

        // Convertir las filas en un array bidimensional
        Object[][] rowData = rows.toArray(new Object[0][]);

        // Crear y devolver el DefaultTableModel
        return new DefaultTableModel(rowData, columnNames);
    }

    public static void main(String[] args) {
        new OrderViewForm();
    }
}
