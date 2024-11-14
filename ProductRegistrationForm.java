import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProductRegistrationForm extends JFrame {
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JTextField stockField;

    public ProductRegistrationForm() {
        setTitle("Registrar Producto");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        // Nombre del producto
        add(new JLabel("Nombre:"));
        nameField = new JTextField();
        add(nameField);

        // Descripción del producto
        add(new JLabel("Descripción:"));
        descriptionField = new JTextField();
        add(descriptionField);

        // Precio del producto
        add(new JLabel("Precio:"));
        priceField = new JTextField();
        add(priceField);

        // Stock del producto
        add(new JLabel("Stock:"));
        stockField = new JTextField();
        add(stockField);

        // Botón para registrar producto
        JButton registerButton = new JButton("Registrar");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerProduct();
            }
        });
        add(registerButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void registerProduct() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());

        String query = "INSERT INTO productos (nombre, descripcion, precio, stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setInt(4, stock);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Producto registrado exitosamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar el producto.");
        }
    }
}
