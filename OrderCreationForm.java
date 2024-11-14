import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class OrderCreationForm extends JFrame {
    private JComboBox<String> productComboBox;
    private JTextField quantityField;
    private JButton createOrderButton;
    private Connection connection;

    public OrderCreationForm() {
        setTitle("Crear Pedido");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // Crear la conexión a la base de datos
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_inventario", "root", "rosario98");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JLabel productLabel = new JLabel("Seleccionar Producto:");
        add(productLabel);

        // ComboBox para seleccionar el producto
        productComboBox = new JComboBox<>();
        loadProducts();
        add(productComboBox);

        JLabel quantityLabel = new JLabel("Cantidad:");
        add(quantityLabel);

        // Campo de texto para ingresar la cantidad
        quantityField = new JTextField(10);
        add(quantityField);

        // Botón para crear el pedido
        createOrderButton = new JButton("Crear Pedido");
        createOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createOrder();
            }
        });
        add(createOrderButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Método para cargar productos desde la base de datos al JComboBox
    private void loadProducts() {
        try {
            String query = "SELECT id_producto, nombre FROM productos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String productName = resultSet.getString("nombre");
                productComboBox.addItem(productName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para crear el pedido y actualizar el stock
    private void createOrder() {
        String selectedProduct = (String) productComboBox.getSelectedItem();
        int quantity = Integer.parseInt(quantityField.getText());

        try {
            // Obtener el ID del producto seleccionado
            String query = "SELECT id_producto, precio FROM productos WHERE nombre = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedProduct);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int productId = resultSet.getInt("id_producto");
                double productPrice = resultSet.getDouble("precio");

                // Crear el pedido
                String insertOrderQuery = "INSERT INTO pedidos (id_cliente, fecha_pedido, total) VALUES (?, CURDATE(), ?)";
                preparedStatement = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, 1);  // Asignar el ID del cliente (esto puede cambiar según la implementación)
                preparedStatement.setDouble(2, productPrice * quantity);
                preparedStatement.executeUpdate();

                // Obtener el ID del pedido recién creado
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int orderId = resultSet.getInt(1);

                    // Insertar en detalle_pedidos
                    String insertOrderDetailsQuery = "INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(insertOrderDetailsQuery);
                    preparedStatement.setInt(1, orderId);
                    preparedStatement.setInt(2, productId);
                    preparedStatement.setInt(3, quantity);
                    preparedStatement.setDouble(4, productPrice * quantity);
                    preparedStatement.executeUpdate();

                    // Actualizar el stock del producto
                    String updateStockQuery = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";
                    preparedStatement = connection.prepareStatement(updateStockQuery);
                    preparedStatement.setInt(1, quantity);
                    preparedStatement.setInt(2, productId);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Pedido creado con éxito!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al crear el pedido.");
        }
    }
}
