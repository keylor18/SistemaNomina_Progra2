package presentacion;

import entidades.Empleado;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import utilidades.FormatoUtil;

/**
 * Panel de mantenimiento de empleados.
 */
public class EmpleadoPanel extends JPanel {

    private final JTextField txtId;
    private final JTextField txtCedula;
    private final JTextField txtNombre;
    private final JTextField txtPuesto;
    private final JTextField txtDepartamento;
    private final JTextField txtCorreo;
    private final JTextField txtSalario;
    private final JSpinner spHijos;
    private final JCheckBox chkConyuge;
    private final JCheckBox chkActivo;
    private final JTextField txtFechaIngreso;
    private final JButton btnGuardar;
    private final JButton btnActualizar;
    private final JButton btnEliminar;
    private final JButton btnLimpiar;
    private final JButton btnRecargar;
    private final JTable tabla;
    private final DefaultTableModel modelo;
    private List<Empleado> empleadosTabla;

    public EmpleadoPanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        empleadosTabla = new ArrayList<>();

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del empleado"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField();
        txtId.setEditable(false);
        txtCedula = new JTextField();
        txtNombre = new JTextField();
        txtPuesto = new JTextField();
        txtDepartamento = new JTextField();
        txtCorreo = new JTextField();
        txtSalario = new JTextField();
        spHijos = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        chkConyuge = new JCheckBox("Conyuge a cargo");
        chkActivo = new JCheckBox("Activo", true);
        txtFechaIngreso = new JTextField(LocalDate.now().toString());

        agregarCampo(formulario, gbc, 0, "ID:", txtId);
        agregarCampo(formulario, gbc, 1, "Cedula:", txtCedula);
        agregarCampo(formulario, gbc, 2, "Nombre completo:", txtNombre);
        agregarCampo(formulario, gbc, 3, "Puesto:", txtPuesto);
        agregarCampo(formulario, gbc, 4, "Departamento:", txtDepartamento);
        agregarCampo(formulario, gbc, 5, "Correo:", txtCorreo);
        agregarCampo(formulario, gbc, 6, "Salario base mensual:", txtSalario);
        agregarCampo(formulario, gbc, 7, "Cantidad de hijos:", spHijos);
        agregarCampo(formulario, gbc, 8, "Fecha ingreso (yyyy-MM-dd):", txtFechaIngreso);

        gbc.gridx = 0;
        gbc.gridy = 9;
        formulario.add(new JLabel("Condicion:"), gbc);
        JPanel checks = new JPanel();
        checks.add(chkConyuge);
        checks.add(chkActivo);
        gbc.gridx = 1;
        formulario.add(checks, gbc);

        JPanel acciones = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnRecargar = new JButton("Recargar");
        acciones.add(btnGuardar);
        acciones.add(btnActualizar);
        acciones.add(btnEliminar);
        acciones.add(btnLimpiar);
        acciones.add(btnRecargar);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        formulario.add(acciones, gbc);

        modelo = new DefaultTableModel(new Object[]{
            "ID", "Cedula", "Nombre", "Puesto", "Departamento", "Correo", "Salario", "Activo"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setPreferredScrollableViewportSize(new Dimension(900, 360));

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    public void setAccionGuardar(ActionListener listener) {
        btnGuardar.addActionListener(listener);
    }

    public void setAccionActualizar(ActionListener listener) {
        btnActualizar.addActionListener(listener);
    }

    public void setAccionEliminar(ActionListener listener) {
        btnEliminar.addActionListener(listener);
    }

    public void setAccionLimpiar(ActionListener listener) {
        btnLimpiar.addActionListener(listener);
    }

    public void setAccionRecargar(ActionListener listener) {
        btnRecargar.addActionListener(listener);
    }

    public void setSelectionListener(ListSelectionListener listener) {
        tabla.getSelectionModel().addListSelectionListener(listener);
    }

    public Empleado construirEmpleadoDesdeFormulario() {
        Empleado empleado = new Empleado();
        empleado.setId(txtId.getText().trim());
        empleado.setCedula(txtCedula.getText().trim());
        empleado.setNombreCompleto(txtNombre.getText().trim());
        empleado.setPuesto(txtPuesto.getText().trim());
        empleado.setDepartamento(txtDepartamento.getText().trim());
        empleado.setCorreoElectronico(txtCorreo.getText().trim());
        empleado.setSalarioBaseMensual(Double.parseDouble(txtSalario.getText().trim()));
        empleado.setCantidadHijos((Integer) spHijos.getValue());
        empleado.setConyugeACargo(chkConyuge.isSelected());
        empleado.setFechaIngreso(LocalDate.parse(txtFechaIngreso.getText().trim()));
        empleado.setActivo(chkActivo.isSelected());
        return empleado;
    }

    public void cargarEmpleadoEnFormulario(Empleado empleado) {
        txtId.setText(empleado.getId());
        txtCedula.setText(empleado.getCedula());
        txtNombre.setText(empleado.getNombreCompleto());
        txtPuesto.setText(empleado.getPuesto());
        txtDepartamento.setText(empleado.getDepartamento());
        txtCorreo.setText(empleado.getCorreoElectronico());
        txtSalario.setText(String.valueOf(empleado.getSalarioBaseMensual()));
        spHijos.setValue(empleado.getCantidadHijos());
        chkConyuge.setSelected(empleado.isConyugeACargo());
        chkActivo.setSelected(empleado.isActivo());
        txtFechaIngreso.setText(empleado.getFechaIngreso().toString());
    }

    public void setEmpleados(List<Empleado> empleados) {
        empleadosTabla = new ArrayList<>(empleados);
        modelo.setRowCount(0);
        for (Empleado empleado : empleados) {
            modelo.addRow(new Object[]{
                empleado.getId(),
                empleado.getCedula(),
                empleado.getNombreCompleto(),
                empleado.getPuesto(),
                empleado.getDepartamento(),
                empleado.getCorreoElectronico(),
                FormatoUtil.formatearMoneda(empleado.getSalarioBaseMensual()),
                empleado.isActivo() ? "Si" : "No"
            });
        }
    }

    public Empleado getEmpleadoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= empleadosTabla.size()) {
            return null;
        }
        return empleadosTabla.get(fila);
    }

    public void limpiarFormulario() {
        txtId.setText("");
        txtCedula.setText("");
        txtNombre.setText("");
        txtPuesto.setText("");
        txtDepartamento.setText("");
        txtCorreo.setText("");
        txtSalario.setText("");
        spHijos.setValue(0);
        chkConyuge.setSelected(false);
        chkActivo.setSelected(true);
        txtFechaIngreso.setText(LocalDate.now().toString());
        tabla.clearSelection();
    }

    public void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, java.awt.Component campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        panel.add(campo, gbc);
    }
}
