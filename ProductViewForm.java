import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProductViewForm extends JFrame {
    private JTable productTable;

    public ProductViewForm() {
        setTitle("Ver Productos");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        productTable = new JTable();
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        loadProducts();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadProducts() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Descripción");
        model.addColumn("Precio");
        model.addColumn("Stock");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM productos";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id_producto");
                String name = resultSet.getString("nombre");
                String description = resultSet.getString("descripcion");
                double price = resultSet.getDouble("precio");
                int stock = resultSet.getInt("stock");
                model.addRow(new Object[]{id, name, description, price, stock});
            }
            productTable.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los productos.");
        }
    }
}
