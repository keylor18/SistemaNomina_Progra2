package presentacion;

import entidades.Empleado;
import entidades.Nomina;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import presentacion.componentes.PanelRedondeado;
import presentacion.componentes.RenderizadorEstadoTabla;
import presentacion.componentes.TarjetaMetrica;
import presentacion.estilo.TemaVisual;
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
    private final JEditorPane panelDetalle;
    private final TarjetaMetrica tarjetaRegistros;
    private final TarjetaMetrica tarjetaBruto;
    private final TarjetaMetrica tarjetaNeto;
    private final TarjetaMetrica tarjetaCosto;
    private final JLabel lblEstadoGeneracion;
    private JLabel lblResumenPeriodo;
    private List<Nomina> nominasTabla;

    public NominaPanel() {
        setLayout(new BorderLayout(18, 18));
        setBackground(TemaVisual.FONDO_APP);
        setBorder(BorderFactory.createEmptyBorder(6, 4, 4, 4));
        nominasTabla = new ArrayList<>();

        tarjetaRegistros = new TarjetaMetrica("Nominas", "0", "Historial registrado en el sistema.");
        tarjetaBruto = new TarjetaMetrica("Bruto total", FormatoUtil.formatearMoneda(0), "Monto salarial del historial visible.");
        tarjetaNeto = new TarjetaMetrica("Neto total", FormatoUtil.formatearMoneda(0), "Pago neto consolidado.");
        tarjetaCosto = new TarjetaMetrica("Costo empresa", FormatoUtil.formatearMoneda(0), "Bruto mas aportes patronales.");
        add(crearResumenSuperior(), BorderLayout.NORTH);

        cmbEmpleados = new JComboBox<>();
        cmbEmpleados.setMaximumRowCount(8);
        spPeriodo = new JSpinner(new SpinnerDateModel());
        spPeriodo.setEditor(new JSpinner.DateEditor(spPeriodo, "MM/yyyy"));
        chkEnviarAutomatico = new JCheckBox("Enviar comprobante al correo del empleado inmediatamente");
        btnGenerar = new JButton("Generar nomina");
        btnExportarSeleccionada = new JButton("PDF individual");
        btnExportarGeneral = new JButton("PDF general");
        btnEnviarCorreo = new JButton("Enviar correo");
        btnRecargar = new JButton("Recargar historial");
        lblEstadoGeneracion = new JLabel("Cargando colaboradores activos...");
        lblEstadoGeneracion.setFont(TemaVisual.fuente(Font.PLAIN, 12));
        lblEstadoGeneracion.setForeground(TemaVisual.TEXTO_SUAVE);

        TemaVisual.estilizarCombo(cmbEmpleados);
        TemaVisual.estilizarSpinner(spPeriodo);
        TemaVisual.estilizarCheck(chkEnviarAutomatico);
        TemaVisual.estilizarBotonPrimario(btnGenerar);
        TemaVisual.estilizarBotonSecundario(btnExportarSeleccionada);
        TemaVisual.estilizarBotonSecundario(btnExportarGeneral);
        TemaVisual.estilizarBotonSecundario(btnEnviarCorreo);
        TemaVisual.estilizarBotonSecundario(btnRecargar);

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
        TemaVisual.estilizarTabla(tabla);
        tabla.getColumnModel().getColumn(7).setCellRenderer(new RenderizadorEstadoTabla());

        panelDetalle = new JEditorPane("text/html", "");
        panelDetalle.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        panelDetalle.setEditable(false);
        panelDetalle.setBackground(java.awt.Color.WHITE);
        panelDetalle.setFont(TemaVisual.fuente(Font.PLAIN, 13));
        mostrarDetalleNomina(null);

        JComponent tarjetaGeneracion = crearTarjetaGeneracion();
        JComponent tarjetaDetalle = crearTarjetaDetalle();
        tarjetaGeneracion.setMinimumSize(new Dimension(360, 300));
        tarjetaDetalle.setMinimumSize(new Dimension(360, 300));

        JSplitPane superior = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tarjetaGeneracion, tarjetaDetalle);
        superior.setOpaque(false);
        superior.setBorder(BorderFactory.createEmptyBorder());
        superior.setDividerSize(12);
        superior.setResizeWeight(0.48);
        superior.setContinuousLayout(true);

        JComponent tarjetaHistorial = crearTarjetaHistorial();
        tarjetaHistorial.setMinimumSize(new Dimension(0, 280));

        JSplitPane contenido = new JSplitPane(JSplitPane.VERTICAL_SPLIT, superior, tarjetaHistorial);
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder());
        contenido.setDividerSize(12);
        contenido.setResizeWeight(0.42);
        contenido.setContinuousLayout(true);
        add(contenido, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            superior.setDividerLocation(0.46);
            contenido.setDividerLocation(0.40);
        });
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
        boolean hayEmpleados = !empleados.isEmpty();
        btnGenerar.setEnabled(hayEmpleados);
        chkEnviarAutomatico.setEnabled(hayEmpleados);
        cmbEmpleados.setEnabled(hayEmpleados);
        if (hayEmpleados) {
            cmbEmpleados.setSelectedIndex(0);
            lblEstadoGeneracion.setForeground(TemaVisual.EXITO);
            lblEstadoGeneracion.setText(empleados.size() == 1
                    ? "1 colaborador activo disponible para generar nomina."
                    : empleados.size() + " colaboradores activos disponibles para generar nomina.");
        } else {
            lblEstadoGeneracion.setForeground(TemaVisual.PELIGRO);
            lblEstadoGeneracion.setText("No hay colaboradores activos. Registre o active uno en la pestana Colaboradores.");
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
        double totalBruto = nominas.stream().mapToDouble(Nomina::getSalarioBruto).sum();
        double totalNeto = nominas.stream().mapToDouble(Nomina::getSalarioNeto).sum();
        double totalCosto = nominas.stream().mapToDouble(Nomina::getCostoTotalEmpresa).sum();
        tarjetaRegistros.actualizar(String.valueOf(nominas.size()), "Nominas almacenadas para consulta y reportes.");
        tarjetaBruto.actualizar(FormatoUtil.formatearMoneda(totalBruto), "Suma del salario bruto registrado.");
        tarjetaNeto.actualizar(FormatoUtil.formatearMoneda(totalNeto), "Monto neto consolidado del historial.");
        tarjetaCosto.actualizar(FormatoUtil.formatearMoneda(totalCosto), "Costo total acumulado para la empresa.");
        lblResumenPeriodo.setText(nominas.isEmpty()
                ? "No existen nominas registradas todavia."
                : "Seleccione una fila para revisar su detalle operativo.");
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
            panelDetalle.setText("<html><body style='font-family:Segoe UI;padding:16px;color:#213645;background:#ffffff;'>"
                    + "<h2 style='margin-top:0;color:#125d5f;'>Detalle de nomina</h2>"
                    + "<p>Seleccione una nomina del historial para visualizar salario bruto, deducciones, "
                    + "aportes patronales, costo total y ruta del comprobante PDF.</p></body></html>");
            return;
        }
        panelDetalle.setText("""
                <html>
                <body style='font-family:Segoe UI;padding:16px;color:#213645;background:#ffffff;'>
                <h2 style='margin-top:0;color:#125d5f;'>%s</h2>
                <p style='color:#667085;margin-top:0;'>Periodo %s | Generada el %s</p>
                <table style='width:100%%;border-collapse:collapse;font-size:13px;'>
                <tr><td style='padding:8px 0;color:#667085;'>Salario bruto</td><td style='padding:8px 0;text-align:right;'><b>%s</b></td></tr>
                <tr><td style='padding:8px 0;color:#667085;'>SEM</td><td style='padding:8px 0;text-align:right;'>%s</td></tr>
                <tr><td style='padding:8px 0;color:#667085;'>IVM</td><td style='padding:8px 0;text-align:right;'>%s</td></tr>
                <tr><td style='padding:8px 0;color:#667085;'>Banco Popular trabajador</td><td style='padding:8px 0;text-align:right;'>%s</td></tr>
                <tr><td style='padding:8px 0;color:#667085;'>Renta salarial</td><td style='padding:8px 0;text-align:right;'>%s</td></tr>
                <tr><td style='padding:8px 0;color:#667085;'><b>Total deducciones</b></td><td style='padding:8px 0;text-align:right;'><b>%s</b></td></tr>
                <tr><td style='padding:8px 0;color:#125d5f;'><b>Salario neto</b></td><td style='padding:8px 0;text-align:right;color:#125d5f;'><b>%s</b></td></tr>
                <tr><td style='padding:8px 0;color:#667085;'>Aportes patronales</td><td style='padding:8px 0;text-align:right;'>%s</td></tr>
                <tr><td style='padding:8px 0;color:#667085;'>Costo total empresa</td><td style='padding:8px 0;text-align:right;'>%s</td></tr>
                </table>
                <p style='margin-top:18px;color:#667085;'><b>PDF:</b> %s</p>
                </body>
                </html>
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
        panelDetalle.setCaretPosition(0);
    }

    public void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel crearResumenSuperior() {
        JPanel resumen = new JPanel(new GridLayout(1, 4, 14, 14));
        resumen.setOpaque(false);
        resumen.add(tarjetaRegistros);
        resumen.add(tarjetaBruto);
        resumen.add(tarjetaNeto);
        resumen.add(tarjetaCosto);
        return resumen;
    }

    private JComponent crearTarjetaGeneracion() {
        PanelRedondeado tarjeta = new PanelRedondeado(TemaVisual.SUPERFICIE, 26);
        tarjeta.setLayout(new BorderLayout(0, 18));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.add(TemaVisual.crearTituloSeccion("Generacion de nomina"));
        encabezado.add(Box.createVerticalStrut(6));
        encabezado.add(TemaVisual.crearSubtitulo("Prepare el periodo, seleccione el colaborador y ejecute la liquidacion."));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 16, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        formulario.add(crearBloqueCampo("Empleado activo", cmbEmpleados), gbc);

        gbc.gridy++;
        formulario.add(crearBloqueCampo("Periodo", spPeriodo), gbc);

        gbc.gridy++;
        formulario.add(chkEnviarAutomatico, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(8, 0, 8, 0);
        formulario.add(lblEstadoGeneracion, gbc);

        JPanel acciones = new JPanel(new GridLayout(3, 2, 10, 10));
        acciones.setOpaque(false);
        acciones.add(btnGenerar);
        acciones.add(btnExportarSeleccionada);
        acciones.add(btnExportarGeneral);
        acciones.add(btnEnviarCorreo);
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
        scroll.setPreferredSize(new Dimension(430, 330));
        return scroll;
    }

    private JComponent crearTarjetaDetalle() {
        PanelRedondeado tarjeta = new PanelRedondeado(TemaVisual.SUPERFICIE, 26);
        tarjeta.setLayout(new BorderLayout(0, 14));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.add(TemaVisual.crearTituloSeccion("Resumen operativo"));
        encabezado.add(Box.createVerticalStrut(6));
        encabezado.add(TemaVisual.crearSubtitulo("Visualice el impacto de la nomina seleccionada antes de exportar o enviar."));

        JScrollPane scroll = new JScrollPane(panelDetalle);
        TemaVisual.estilizarScroll(scroll);

        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(scroll, BorderLayout.CENTER);
        tarjeta.setPreferredSize(new Dimension(430, 330));
        return tarjeta;
    }

    private JComponent crearTarjetaHistorial() {
        PanelRedondeado tarjeta = new PanelRedondeado(TemaVisual.SUPERFICIE, 26);
        tarjeta.setLayout(new BorderLayout(0, 14));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        tarjeta.setPreferredSize(new Dimension(0, 360));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(TemaVisual.crearTituloSeccion("Historial de nominas"));
        textos.add(Box.createVerticalStrut(6));
        lblResumenPeriodo = new JLabel("Cargando historial...");
        lblResumenPeriodo.setFont(TemaVisual.fuente(Font.PLAIN, 13));
        lblResumenPeriodo.setForeground(TemaVisual.TEXTO_SUAVE);
        textos.add(lblResumenPeriodo);
        encabezado.add(textos, BorderLayout.WEST);

        JPanel ayuda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        ayuda.setOpaque(false);
        JLabel soporte = new JLabel("Use la tabla para regenerar PDF o reenviar comprobantes.");
        soporte.setFont(TemaVisual.fuente(Font.PLAIN, 12));
        soporte.setForeground(TemaVisual.TEXTO_SUAVE);
        ayuda.add(soporte);
        encabezado.add(ayuda, BorderLayout.EAST);

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
