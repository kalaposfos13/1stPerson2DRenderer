package nhf;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;



public class EditorWindow extends JFrame {

   private final WorkAreaPanel workArea;
   private Color selectedColor = Color.BLACK;

   public EditorWindow() {
      setTitle("2D Scene Editor");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(800, 600);
      setLocationRelativeTo(null);

      // Create a new panel for the toolbar and the color button
      JToolBar toolBar = new JToolBar();
      toolBar.setLayout(new BorderLayout()); // Use BorderLayout to place elements on left and right sides

      // Left side toolbar buttons
      Box leftToolBarBox = Box.createHorizontalBox();
      JButton saveButton = new JButton("Save to File");
      JButton loadButton = new JButton("Load from File");
      JButton clearButton = new JButton("Clear Current Scene");
      JButton openRendererButton = new JButton("Open Renderer");

      leftToolBarBox.add(saveButton);
      leftToolBarBox.add(loadButton);
      leftToolBarBox.add(clearButton);
      leftToolBarBox.add(openRendererButton);


      toolBar.add(leftToolBarBox, BorderLayout.WEST);

      // Add color picker button
      JButton colorButton = new JButton("Color");
      colorButton.setBackground(selectedColor);
      colorButton.setOpaque(true);
      colorButton.setBorderPainted(false);
      toolBar.add(colorButton);

      // Right side color button
      Box rightToolBarBox = Box.createHorizontalBox();
      rightToolBarBox.add(colorButton);

      toolBar.add(rightToolBarBox, BorderLayout.EAST); // Align color button to the right


      // Create color picker panel and add it to the layered pane
      JPanel colorPickerPanel = createColorPickerPanel(colorButton);
      colorPickerPanel.setVisible(false);
      getLayeredPane().add(colorPickerPanel, JLayeredPane.POPUP_LAYER);

      // Show/hide color picker panel on color button click
      colorButton.addActionListener(e -> {
         // Toggle visibility and adjust location
         if (!colorPickerPanel.isVisible()) {
            Point buttonLocation = colorButton.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(buttonLocation, getLayeredPane());
            colorPickerPanel.setLocation(
                  buttonLocation.x + colorButton.getWidth() - colorPickerPanel.getWidth(),
                  buttonLocation.y + colorButton.getHeight() + 5);
            colorPickerPanel.setVisible(true);
         } else {
            colorPickerPanel.setVisible(false);
         }
      });

      workArea = new WorkAreaPanel(new Scene("1.0"));
      getContentPane().add(toolBar, BorderLayout.NORTH);
      getContentPane().add(workArea, BorderLayout.CENTER);

      saveButton.addActionListener(e -> saveScene());
      loadButton.addActionListener(e -> loadScene());
      clearButton.addActionListener(e -> clearScene());
      openRendererButton.addActionListener(e -> openRendererWindow());

      setVisible(true);
   }

   private JPanel createColorPickerPanel(JButton colorButton) {
      JPanel colorPickerPanel = new JPanel(new BorderLayout());
      colorPickerPanel.setSize(200, 84);

      // Create table with six columns and no headers
      JTable colorTable = getColorJTable();

      // Set custom cell renderer for colors
      for (int i = 0; i < colorTable.getColumnCount(); i++) {
         colorTable.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
               JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
               label.setOpaque(true);
               if (value instanceof Color) {
                  label.setBackground((Color) value);
                  label.setText("");
               }
               return label;
            }
         });
      }

      // Update the table selection listener to handle clicks on each cell without duplicate events
      colorTable.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            int row = colorTable.rowAtPoint(e.getPoint());
            int col = colorTable.columnAtPoint(e.getPoint());
            if (row != -1 && col != -1) {
               Color clickedColor = (Color) colorTable.getValueAt(row, col);
               if (clickedColor != null) {
                  selectedColor = clickedColor;
                  workArea.paintColor = clickedColor;
                  colorButton.setBackground(selectedColor);
                  colorPickerPanel.setVisible(false);  // Close color picker panel
                  if (workArea.getSelectedLine() != null) {
                     workArea.getSelectedLine().color = selectedColor;
                     workArea.repaint();
                  }
               }
            }
         }
      });

      // Custom color input
      JTextField hexInputField = new JTextField("#000000");
      hexInputField.addActionListener(e -> {
         try {
            selectedColor = Color.decode(hexInputField.getText());
            workArea.paintColor = selectedColor;
            colorButton.setBackground(selectedColor);
            colorPickerPanel.setVisible(false);
         } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid hex color format. Use #RRGGBBAA.");
         }
      });

      colorPickerPanel.add(new JScrollPane(colorTable), BorderLayout.CENTER);
      colorPickerPanel.add(hexInputField, BorderLayout.SOUTH);

      return colorPickerPanel;
   }

   private static JTable getColorJTable() {
      Color[][] colors = {
            {Color.WHITE, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.BLACK, new Color(255, 165, 0), new Color(139, 69, 19)},    // Row 1
            {Color.PINK, Color.RED, new Color(144, 238, 144), Color.GREEN, new Color(173, 216, 230), Color.BLUE}, // Row 2
            {new Color(0,128,0), Color.MAGENTA, new Color(128, 0, 128), Color.CYAN, Color.YELLOW, new Color(139, 0, 0)} // Row 3
      };


      JTable colorTable = new JTable(new DefaultTableModel(colors, new String[6]) {
         @Override
         public Class<?> getColumnClass(int column) {
            return Color.class;
         }

         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // Make cells non-editable
         }
      });

      colorTable.setTableHeader(null);
      colorTable.setRowHeight(20);
      return colorTable;
   }

   private void clearScene() {
      workArea.currentScene = new Scene("1.0");
      workArea.repaint();
   }

   private void openRendererWindow() {
      new RendererWindow(workArea.currentScene);
   }

   private void saveScene() {
      if (workArea.currentScene == null) {
         JOptionPane.showMessageDialog(this, "No scene to save!", "Error", JOptionPane.ERROR_MESSAGE);
         return;
      }

      JFileChooser fileChooser = new JFileChooser("./");
      int returnValue = fileChooser.showSaveDialog(this);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
         File file = fileChooser.getSelectedFile();
         try {
            SceneFileHandler.saveSceneToXML(workArea.currentScene, file);
            JOptionPane.showMessageDialog(this, "Scene saved successfully!");
         } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save scene!", "Error", JOptionPane.ERROR_MESSAGE);
         }
      }
   }

   private void loadScene() {
      JFileChooser fileChooser = new JFileChooser("./");
      int returnValue = fileChooser.showOpenDialog(this);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
         File file = fileChooser.getSelectedFile();
         try {
            workArea.currentScene = SceneFileHandler.loadSceneFromXML(file);
            workArea.repaint();  // Refresh work area after loading new scene
            //JOptionPane.showMessageDialog(this, "Scene loaded successfully!");
         } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load scene!", "Error", JOptionPane.ERROR_MESSAGE);
         }
      }
   }

   public static void main(String[] args) throws Exception {
      EditorWindow window = new EditorWindow();
      window.workArea.currentScene = SceneFileHandler.loadSceneFromXML(new File("src/main/resources/nhf/treeScene.xml"));
      window.workArea.repaint();
   }
}
