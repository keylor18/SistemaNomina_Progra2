package presentacion;

import entidades.Empleado;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import presentacion.componentes.PanelRedondeado;
import presentacion.componentes.RenderizadorEstadoTabla;
import presentacion.componentes.TarjetaMetrica;
import presentacion.estilo.TemaVisual;
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
    private final JSpinner spFechaIngreso;
    private final JButton btnGuardar;
    private final JButton btnActualizar;
    private final JButton btnEliminar;
    private final JButton btnLimpiar;
    private final JButton btnRecargar;
    private final JTable tabla;
    private final DefaultTableModel modelo;
    private final TarjetaMetrica tarjetaTotal;
    private final TarjetaMetrica tarjetaActivos;
    private final TarjetaMetrica tarjetaPlanilla;
    private JLabel lblResumen;
    private List<Empleado> empleadosTabla;

    public EmpleadoPanel() {
        setLayout(new BorderLayout(18, 18));
        setBackground(TemaVisual.FONDO_APP);
        setBorder(BorderFactory.createEmptyBorder(6, 4, 4, 4));
        empleadosTabla = new ArrayList<>();

        tarjetaTotal = new TarjetaMetrica("Colaboradores", "0", "Registros totales del catalogo.");
        tarjetaActivos = new TarjetaMetrica("Activos", "0", "Colaboradores disponibles para nomina.");
        tarjetaPlanilla = new TarjetaMetrica("Base salarial", FormatoUtil.formatearMoneda(0), "Suma mensual de salarios base.");
        add(crearResumenSuperior(), BorderLayout.NORTH);

        txtId = new JTextField();
        txtCedula = new JTextField();
        txtNombre = new JTextField();
        txtPuesto = new JTextField();
        txtDepartamento = new JTextField();
        txtCorreo = new JTextField();
        txtSalario = new JTextField();
        spHijos = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        chkConyuge = new JCheckBox("Conyuge a cargo");
        chkActivo = new JCheckBox("Empleado activo", true);
        spFechaIngreso = new JSpinner(new SpinnerDateModel());
        spFechaIngreso.setValue(new Date());
        spFechaIngreso.setEditor(new JSpinner.DateEditor(spFechaIngreso, "yyyy-MM-dd"));

        TemaVisual.estilizarCampo(txtId);
        TemaVisual.estilizarCampo(txtCedula);
        TemaVisual.estilizarCampo(txtNombre);
        TemaVisual.estilizarCampo(txtPuesto);
        TemaVisual.estilizarCampo(txtDepartamento);
        TemaVisual.estilizarCampo(txtCorreo);
        TemaVisual.estilizarCampo(txtSalario);
        txtSalario.setToolTipText("Ejemplos: 450000 | 450000,50 | 450000.50");
        TemaVisual.estilizarSpinner(spHijos);
        TemaVisual.estilizarSpinner(spFechaIngreso);
        TemaVisual.estilizarCheck(chkConyuge);
        TemaVisual.estilizarCheck(chkActivo);
        txtId.setEditable(false);
        txtId.setBackground(TemaVisual.SUPERFICIE_SECUNDARIA);

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnRecargar = new JButton("Recargar");
        TemaVisual.estilizarBotonPrimario(btnGuardar);
        TemaVisual.estilizarBotonSecundario(btnActualizar);
        TemaVisual.estilizarBotonPeligro(btnEliminar);
        TemaVisual.estilizarBotonSecundario(btnLimpiar);
        TemaVisual.estilizarBotonSecundario(btnRecargar);

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
        tabla.setPreferredScrollableViewportSize(new Dimension(920, 460));
        TemaVisual.estilizarTabla(tabla);
        tabla.getColumnModel().getColumn(7).setCellRenderer(new RenderizadorEstadoTabla());

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearTarjetaFormulario(), crearTarjetaTabla());
        split.setOpaque(false);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setDividerLocation(420);
        split.setDividerSize(12);
        split.setResizeWeight(0.42);
        add(split, BorderLayout.CENTER);
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
        empleado.setSalarioBaseMensual(FormatoUtil.parsearMonto(txtSalario.getText().trim()));
        empleado.setCantidadHijos((Integer) spHijos.getValue());
        empleado.setConyugeACargo(chkConyuge.isSelected());
        empleado.setFechaIngreso(((Date) spFechaIngreso.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate());
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
        spFechaIngreso.setValue(Date.from(empleado.getFechaIngreso().atStartOfDay(ZoneId.systemDefault()).toInstant()));
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
        long activos = empleados.stream().filter(Empleado::isActivo).count();
        double totalPlanilla = empleados.stream().mapToDouble(Empleado::getSalarioBaseMensual).sum();
        tarjetaTotal.actualizar(String.valueOf(empleados.size()), "Registros cargados en memoria.");
        tarjetaActivos.actualizar(String.valueOf(activos), "Participan en la generacion de nomina.");
        tarjetaPlanilla.actualizar(FormatoUtil.formatearMoneda(totalPlanilla), "Suma mensual del salario base.");
        lblResumen.setText(empleados.isEmpty()
                ? "No hay colaboradores registrados todavia."
                : "Seleccione un colaborador para editarlo o eliminarlo.");
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
        spFechaIngreso.setValue(new Date());
        tabla.clearSelection();
    }

    public void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel crearResumenSuperior() {
        JPanel resumen = new JPanel(new GridLayout(1, 3, 14, 14));
        resumen.setOpaque(false);
        resumen.add(tarjetaTotal);
        resumen.add(tarjetaActivos);
        resumen.add(tarjetaPlanilla);
        return resumen;
    }

    private JComponent crearTarjetaFormulario() {
        PanelRedondeado tarjeta = new PanelRedondeado(TemaVisual.SUPERFICIE, 26);
        tarjeta.setLayout(new BorderLayout(0, 18));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.add(TemaVisual.crearTituloSeccion("Ficha del colaborador"));
        encabezado.add(Box.createVerticalStrut(6));
        encabezado.add(TemaVisual.crearSubtitulo("Complete, actualice o depure el catalogo interno de empleados."));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 14, 14);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(crearBloqueCampo("ID interno", txtId), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 14, 0);
        formulario.add(crearBloqueCampo("Cedula", txtCedula), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 14, 0);
        formulario.add(crearBloqueCampo("Nombre completo", txtNombre), gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 14, 14);
        formulario.add(crearBloqueCampo("Puesto", txtPuesto), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 14, 0);
        formulario.add(crearBloqueCampo("Departamento", txtDepartamento), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formulario.add(crearBloqueCampo("Correo electronico", txtCorreo), gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 14, 14);
        formulario.add(crearBloqueCampo("Salario base mensual", txtSalario), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 14, 0);
        formulario.add(crearBloqueCampo("Cantidad de hijos", spHijos), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formulario.add(crearBloqueCampo("Fecha de ingreso", spFechaIngreso), gbc);

        JPanel checks = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        checks.setOpaque(false);
        checks.add(chkConyuge);
        checks.add(chkActivo);

        gbc.gridy++;
        formulario.add(checks, gbc);

        JPanel acciones = new JPanel(new GridLayout(3, 2, 10, 10));
        acciones.setOpaque(false);
        acciones.add(btnGuardar);
        acciones.add(btnActualizar);
        acciones.add(btnEliminar);
        acciones.add(btnLimpiar);
        acciones.add(btnRecargar);

        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(formulario, BorderLayout.CENTER);
        tarjeta.add(acciones, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(tarjeta);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JComponent crearTarjetaTabla() {
        PanelRedondeado tarjeta = new PanelRedondeado(TemaVisual.SUPERFICIE, 26);
        tarjeta.setLayout(new BorderLayout(0, 14));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.add(TemaVisual.crearTituloSeccion("Directorio de empleados"));
        encabezado.add(Box.createVerticalStrut(6));
        lblResumen = new JLabel("Cargando colaboradores...");
        lblResumen.setFont(TemaVisual.fuente(Font.PLAIN, 13));
        lblResumen.setForeground(TemaVisual.TEXTO_SUAVE);
        encabezado.add(lblResumen);

        JScrollPane scroll = new JScrollPane(tabla);
        TemaVisual.estilizarScroll(scroll);

        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(scroll, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel crearBloqueCampo(String etiqueta, JComponent campo) {
        JPanel bloque = new JPanel();
        bloque.setOpaque(false);
        bloque.setLayout(new BoxLayout(bloque, BoxLayout.Y_AXIS));
        bloque.add(TemaVisual.crearEtiquetaCampo(etiqueta));
        bloque.add(Box.createVerticalStrut(8));
        bloque.add(campo);
        return bloque;
    }
}
