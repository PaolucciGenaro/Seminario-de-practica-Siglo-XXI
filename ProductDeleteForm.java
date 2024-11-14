import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProductDeleteForm extends JFrame {
    private JTextField idField;
    private JButton deleteButton;

    public ProductDeleteForm() {
        setTitle("Eliminar Producto");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        // Campo de entrada para el ID del producto a eliminar
        add(new JLabel("ID del Producto:"));
        idField = new JTextField();
        add(idField);

        // Botón para eliminar el producto
        deleteButton = new JButton("Eliminar Producto");
        add(deleteButton);

        // Acción para el botón de eliminar
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void deleteProduct() {
        String idText = idField.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un ID de producto.");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID de producto inválido.");
            return;
        }

        // Conectar a la base de datos y eliminar el producto
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_inventario", "root", "rosario98")) {
            String deleteSQL = "DELETE FROM productos WHERE id_producto = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                pstmt.setInt(1, productId);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró un producto con el ID especificado.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos o eliminar el producto.");
        }
    }

    public static void main(String[] args) {
        new ProductDeleteForm();
    }
}
