package presentacion;

import entidades.Empleado;
import entidades.Nomina;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import utilidades.FormatoUtil;

/**
 * Panel de calculo, historial y reporteria de nomina.
 */
public class NominaPanel extends JPanel {

    private final JComboBox<Empleado> cmbEmpleados;
    private final JSpinner spPeriodo;
    private final JCheckBox chkEnviarAutomatico;
    private final JButton btnGenerar;
    private final JButton btnExportarSeleccionada;
    private final JButton btnExportarGeneral;
    private final JButton btnEnviarCorreo;
    private final JButton btnRecargar;
    private final JTable tabla;
    private final DefaultTableModel modelo;
    private final JTextArea areaDetalle;
    private List<Nomina> nominasTabla;

    public NominaPanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        nominasTabla = new ArrayList<>();

        JPanel filtros = new JPanel(new GridBagLayout());
        filtros.setBorder(BorderFactory.createTitledBorder("Generacion de nomina"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbEmpleados = new JComboBox<>();
        spPeriodo = new JSpinner(new SpinnerDateModel());
        spPeriodo.setEditor(new JSpinner.DateEditor(spPeriodo, "MM/yyyy"));
        chkEnviarAutomatico = new JCheckBox("Enviar PDF automaticamente al generar");
        btnGenerar = new JButton("Generar nomina");
        btnExportarSeleccionada = new JButton("Exportar PDF individual");
        btnExportarGeneral = new JButton("Exportar PDF general");
        btnEnviarCorreo = new JButton("Enviar correo");
        btnRecargar = new JButton("Recargar");

        agregarCampo(filtros, gbc, 0, "Empleado:", cmbEmpleados);
        agregarCampo(filtros, gbc, 1, "Periodo:", spPeriodo);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        filtros.add(chkEnviarAutomatico, gbc);

        JPanel acciones = new JPanel();
        acciones.add(btnGenerar);
        acciones.add(btnExportarSeleccionada);
        acciones.add(btnExportarGeneral);
        acciones.add(btnEnviarCorreo);
        acciones.add(btnRecargar);
        gbc.gridy = 3;
        filtros.add(acciones, gbc);

        modelo = new DefaultTableModel(new Object[]{
            "ID", "Empleado", "Periodo", "Bruto", "Deducciones", "Aportes", "Neto", "PDF"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setPreferredScrollableViewportSize(new Dimension(960, 300));

        areaDetalle = new JTextArea(12, 20);
        areaDetalle.setEditable(false);
        areaDetalle.setLineWrap(true);
        areaDetalle.setWrapStyleWord(true);
        areaDetalle.setBorder(BorderFactory.createTitledBorder("Detalle de nomina"));

        add(filtros, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(new JScrollPane(areaDetalle), BorderLayout.SOUTH);
    }

    public void setAccionGenerar(ActionListener listener) {
        btnGenerar.addActionListener(listener);
    }

    public void setAccionExportarSeleccionada(ActionListener listener) {
        btnExportarSeleccionada.addActionListener(listener);
    }

    public void setAccionExportarGeneral(ActionListener listener) {
        btnExportarGeneral.addActionListener(listener);
    }

    public void setAccionEnviarCorreo(ActionListener listener) {
        btnEnviarCorreo.addActionListener(listener);
    }

    public void setAccionRecargar(ActionListener listener) {
        btnRecargar.addActionListener(listener);
    }

    public void setSelectionListener(ListSelectionListener listener) {
        tabla.getSelectionModel().addListSelectionListener(listener);
    }

    public void setEmpleados(List<Empleado> empleados) {
        cmbEmpleados.removeAllItems();
        for (Empleado empleado : empleados) {
            cmbEmpleados.addItem(empleado);
        }
    }

    public Empleado getEmpleadoSeleccionado() {
        return (Empleado) cmbEmpleados.getSelectedItem();
    }

    public YearMonth getPeriodoSeleccionado() {
        Date fecha = (Date) spPeriodo.getValue();
        return YearMonth.from(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public boolean isEnviarAutomatico() {
        return chkEnviarAutomatico.isSelected();
    }

    public void setNominas(List<Nomina> nominas) {
        nominasTabla = new ArrayList<>(nominas);
        modelo.setRowCount(0);
        for (Nomina nomina : nominas) {
            modelo.addRow(new Object[]{
                nomina.getId(),
                nomina.getNombreEmpleado(),
                FormatoUtil.formatearPeriodo(nomina.getPeriodo()),
                FormatoUtil.formatearMoneda(nomina.getSalarioBruto()),
                FormatoUtil.formatearMoneda(nomina.getTotalDeducciones()),
                FormatoUtil.formatearMoneda(nomina.getTotalAportesPatronales()),
                FormatoUtil.formatearMoneda(nomina.getSalarioNeto()),
                nomina.getRutaPdf() == null || nomina.getRutaPdf().isBlank() ? "Pendiente" : "Generado"
            });
        }
    }

    public Nomina getNominaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= nominasTabla.size()) {
            return null;
        }
        return nominasTabla.get(fila);
    }

    public void mostrarDetalleNomina(Nomina nomina) {
        if (nomina == null) {
            areaDetalle.setText("Seleccione una nomina para ver el detalle.");
            return;
        }
        areaDetalle.setText("""
                Empleado: %s
                Periodo: %s
                Fecha generacion: %s

                Salario bruto: %s
                Deduccion SEM: %s
                Deduccion IVM: %s
                Banco Popular trabajador: %s
                Impuesto sobre la renta: %s
                Total deducciones: %s
                Salario neto: %s

                Total aportes patronales: %s
                Costo total empresa: %s

                Ruta PDF: %s
                """
                .formatted(
                        nomina.getNombreEmpleado(),
                        FormatoUtil.formatearPeriodo(nomina.getPeriodo()),
                        FormatoUtil.formatearFecha(nomina.getFechaGeneracion()),
                        FormatoUtil.formatearMoneda(nomina.getSalarioBruto()),
                        FormatoUtil.formatearMoneda(nomina.getDeduccionSem()),
                        FormatoUtil.formatearMoneda(nomina.getDeduccionIvm()),
                        FormatoUtil.formatearMoneda(nomina.getDeduccionBancoPopular()),
                        FormatoUtil.formatearMoneda(nomina.getDeduccionRenta()),
                        FormatoUtil.formatearMoneda(nomina.getTotalDeducciones()),
                        FormatoUtil.formatearMoneda(nomina.getSalarioNeto()),
                        FormatoUtil.formatearMoneda(nomina.getTotalAportesPatronales()),
                        FormatoUtil.formatearMoneda(nomina.getCostoTotalEmpresa()),
                        nomina.getRutaPdf() == null || nomina.getRutaPdf().isBlank() ? "Sin generar" : nomina.getRutaPdf()));
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
