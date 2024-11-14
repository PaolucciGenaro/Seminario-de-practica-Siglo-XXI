import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryManagementApp extends JFrame {
    public InventoryManagementApp() {
        setTitle("Sistema de Gestión de Inventario");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton registerProductButton = new JButton("Registrar Producto");
        registerProductButton.setBounds(100, 30, 200, 30);
        registerProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProductRegistrationForm();
            }
        });
        add(registerProductButton);

        JButton viewProductsButton = new JButton("Ver Productos");
        viewProductsButton.setBounds(100, 80, 200, 30);
        viewProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProductViewForm();
            }
        });
        add(viewProductsButton);

        JButton createOrderButton = new JButton("Crear Pedido");
        createOrderButton.setBounds(100, 130, 200, 30);
        createOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OrderCreationForm();
            }
        });
        add(createOrderButton);

        JButton viewOrdersButton = new JButton("Ver Pedidos");
        viewOrdersButton.setBounds(100, 180, 200, 30);
        viewOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OrderViewForm();
            }
        });
        add(viewOrdersButton);

        // Botón para actualizar el stock
        JButton updateStockButton = new JButton("Actualizar Stock");
        updateStockButton.setBounds(100, 230, 200, 30);
        updateStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StockUpdateForm();
            }
        });
        add(updateStockButton);

        //botón para eliminar producto
        JButton deleteProductButton = new JButton("Eliminar Producto");
        deleteProductButton.setBounds(100, 280, 200, 30);
        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProductDeleteForm();
            }
        });
        add(deleteProductButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new InventoryManagementApp();
    }
}
