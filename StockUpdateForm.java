import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StockUpdateForm extends JFrame {
    private JTextField productIdField;
    private JTextField stockField;
    private JButton updateStockButton;

    public StockUpdateForm() {
        setTitle("Actualizar Stock de Producto");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel productIdLabel = new JLabel("ID del Producto:");
        productIdLabel.setBounds(30, 30, 100, 25);
        add(productIdLabel);

        productIdField = new JTextField();
        productIdField.setBounds(150, 30, 200, 25);
        add(productIdField);

        JLabel stockLabel = new JLabel("Nuevo Stock:");
        stockLabel.setBounds(30, 70, 100, 25);
        add(stockLabel);

        stockField = new JTextField();
        stockField.setBounds(150, 70, 200, 25);
        add(stockField);

        updateStockButton = new JButton("Actualizar Stock");
        updateStockButton.setBounds(150, 110, 200, 25);
        add(updateStockButton);

        // Acción para actualizar el stock del producto
        updateStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStock();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateStock() {
        int productId;
        int newStock;
        
        // Validar entradas
        try {
            productId = Integer.parseInt(productIdField.getText());
            newStock = Integer.parseInt(stockField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Conexión a la base de datos y actualización del stock
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_inventario", "root", "rosario98")) {
            String query = "UPDATE productos SET stock = ? WHERE id_producto = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, newStock);
            stmt.setInt(2, productId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Stock actualizado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el stock: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
