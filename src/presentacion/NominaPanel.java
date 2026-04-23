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
import javax.swing.JTextField;
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
 * Panel de cálculo, historial y reportería de nómina.
 */
public class NominaPanel extends JPanel {

    private final JComboBox<Empleado> cmbEmpleados;
    private final JSpinner spPeriodo;
    private final JCheckBox chkEnviarAutomatico;
    private final JCheckBox chkEnviarPatrono;
    private final JTextField txtCorreoPatrono;
    private final JButton btnGenerar;
    private final JButton btnExportarSeleccionada;
    private final JButton btnExportarGeneral;
    private final JButton btnEnviarCorreo;
    private final JButton btnEnviarCorreoPatrono;
    private final JButton btnRecargar;
    private final JButton btnEliminar;
    private final JTable tabla;
    private final DefaultTableModel modelo;
    private final JEditorPane panelDetalle;
    private final TarjetaMetrica tarjetaRegistros;
    private final TarjetaMetrica tarjetaBruto;
    private final TarjetaMetrica tarjetaNeto;
    private final TarjetaMetrica tarjetaCosto;
    private final JLabel lblEstadoGeneracion;
    private JLabel lblResumenPeriodo;
    private JPanel panelResumenCards;
    private JButton btnToggleResumen;
    private List<Nomina> nominasTabla;
    private Nomina nominaDetalle;

    public NominaPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(TemaVisual.FONDO_APP);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        nominasTabla = new ArrayList<>();

        tarjetaRegistros = new TarjetaMetrica("Nóminas", "0", "Historial registrado en el sistema.");
        tarjetaBruto = new TarjetaMetrica("Bruto total", FormatoUtil.formatearMoneda(0), "Monto salarial del historial visible.");
        tarjetaNeto = new TarjetaMetrica("Neto total", FormatoUtil.formatearMoneda(0), "Pago neto consolidado.");
        tarjetaCosto = new TarjetaMetrica("Costo empresa", FormatoUtil.formatearMoneda(0), "Bruto más aportes patronales.");
        add(crearResumenSuperior(), BorderLayout.NORTH);

        cmbEmpleados = new JComboBox<>();
        cmbEmpleados.setMaximumRowCount(8);
        spPeriodo = new JSpinner(new SpinnerDateModel());
        spPeriodo.setEditor(new JSpinner.DateEditor(spPeriodo, "MM/yyyy"));
        chkEnviarAutomatico = new JCheckBox("Enviar comprobante al correo del empleado inmediatamente");
        chkEnviarPatrono = new JCheckBox("Enviar reporte patronal al correo indicado");
        txtCorreoPatrono = new JTextField();
        btnGenerar = new JButton("Generar nómina");
        btnExportarSeleccionada = new JButton("PDF individual");
        btnExportarGeneral = new JButton("PDF general");
        btnEnviarCorreo = new JButton("Enviar correo");
        btnEnviarCorreoPatrono = new JButton("Enviar al patrono");
        btnRecargar = new JButton("Recargar historial");
        btnEliminar = new JButton("Eliminar registro");
        lblEstadoGeneracion = new JLabel("Cargando colaboradores activos...");
        lblEstadoGeneracion.setFont(TemaVisual.fuente(Font.PLAIN, 12));
        lblEstadoGeneracion.setForeground(TemaVisual.TEXTO_SUAVE);

        TemaVisual.estilizarCombo(cmbEmpleados);
        TemaVisual.estilizarSpinner(spPeriodo);
        TemaVisual.estilizarCheck(chkEnviarAutomatico);
        TemaVisual.estilizarCheck(chkEnviarPatrono);
        TemaVisual.estilizarCampo(txtCorreoPatrono);
        TemaVisual.estilizarBotonPrimario(btnGenerar);
        TemaVisual.estilizarBotonSecundario(btnExportarSeleccionada);
        TemaVisual.estilizarBotonSecundario(btnExportarGeneral);
        TemaVisual.estilizarBotonSecundario(btnEnviarCorreo);
        TemaVisual.estilizarBotonSecundario(btnEnviarCorreoPatrono);
        TemaVisual.estilizarBotonSecundario(btnRecargar);
        TemaVisual.estilizarBotonPeligro(btnEliminar);
        txtCorreoPatrono.setToolTipText("Correo del patrono para enviar el reporte patronal separado");

        modelo = new DefaultTableModel(new Object[]{
            "ID", "Empleado", "Período", "Bruto", "Deducciones", "Aportes", "Neto", "PDF"
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
        contenido.setDividerSize(10);
        contenido.setResizeWeight(0.42);
        contenido.setContinuousLayout(true);

        JScrollPane scrollGeneral = new JScrollPane(contenido);
        scrollGeneral.setBorder(BorderFactory.createEmptyBorder());
        scrollGeneral.setOpaque(false);
        scrollGeneral.getViewport().setOpaque(false);
        scrollGeneral.getVerticalScrollBar().setUnitIncrement(16);
        scrollGeneral.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollGeneral, BorderLayout.CENTER);

        TemaVisual.addTemaListener(() ->
                SwingUtilities.invokeLater(() -> mostrarDetalleNomina(nominaDetalle)));

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

    public void setAccionEnviarCorreoPatrono(ActionListener listener) {
        btnEnviarCorreoPatrono.addActionListener(listener);
    }

    public void setAccionRecargar(ActionListener listener) {
        btnRecargar.addActionListener(listener);
    }

    public void setAccionEliminar(ActionListener listener) {
        btnEliminar.addActionListener(listener);
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
        chkEnviarPatrono.setEnabled(hayEmpleados);
        cmbEmpleados.setEnabled(hayEmpleados);
        txtCorreoPatrono.setEnabled(hayEmpleados);
        btnEnviarCorreoPatrono.setEnabled(hayEmpleados);
        if (hayEmpleados) {
            cmbEmpleados.setSelectedIndex(0);
            lblEstadoGeneracion.setForeground(TemaVisual.EXITO);
            lblEstadoGeneracion.setText(empleados.size() == 1
                    ? "1 colaborador activo disponible para generar nómina."
                    : empleados.size() + " colaboradores activos disponibles para generar nómina.");
        } else {
            lblEstadoGeneracion.setForeground(TemaVisual.PELIGRO);
            lblEstadoGeneracion.setText("No hay colaboradores activos. Registre o active uno en la pestaña Colaboradores.");
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

    public boolean isEnviarPatronoAutomatico() {
        return chkEnviarPatrono.isSelected();
    }

    public String getCorreoPatrono() {
        return txtCorreoPatrono.getText().trim();
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
        tarjetaRegistros.actualizar(String.valueOf(nominas.size()), "Nóminas almacenadas para consulta y reportes.");
        tarjetaBruto.actualizar(FormatoUtil.formatearMoneda(totalBruto), "Suma del salario bruto registrado.");
        tarjetaNeto.actualizar(FormatoUtil.formatearMoneda(totalNeto), "Monto neto consolidado del historial.");
        tarjetaCosto.actualizar(FormatoUtil.formatearMoneda(totalCosto), "Costo total acumulado para la empresa.");
        lblResumenPeriodo.setText(nominas.isEmpty()
                ? "No existen nóminas registradas todavía."
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
        nominaDetalle = nomina;
        String cTexto = TemaVisual.colorHex(TemaVisual.TEXTO);
        String cSuave = TemaVisual.colorHex(TemaVisual.TEXTO_SUAVE);
        String cFondo = TemaVisual.colorHex(TemaVisual.SUPERFICIE);
        String cPrimario = TemaVisual.colorHex(TemaVisual.PRIMARIO);
        panelDetalle.setBackground(TemaVisual.isModoOscuro() ? TemaVisual.SUPERFICIE : java.awt.Color.WHITE);

        if (nomina == null) {
            panelDetalle.setText("<html><body style='font-family:Segoe UI;padding:16px;"
                    + "color:" + cTexto + ";background:" + cFondo + ";'>"
                    + "<h2 style='margin-top:0;color:" + cPrimario + ";'>Detalle de nómina</h2>"
                    + "<p>Seleccione una nómina del historial para visualizar salario bruto, deducciones, "
                    + "aportes patronales, costo total y ruta del comprobante PDF.</p>"
                    + "</body></html>");
            return;
        }
        panelDetalle.setText(("<html>"
                + "<body style='font-family:Segoe UI;padding:16px;color:%s;background:%s;'>"
                + "<h2 style='margin-top:0;color:%s;'>%s</h2>"
                + "<p style='color:%s;margin-top:0;'>Período %s | Generada el %s</p>"
                + "<table style='width:100%%;border-collapse:collapse;font-size:13px;'>"
                + "<tr><td style='padding:8px 0;color:%s;'>Salario bruto</td>"
                + "    <td style='padding:8px 0;text-align:right;'><b>%s</b></td></tr>"
                + "<tr><td style='padding:8px 0;color:%s;'>SEM</td>"
                + "    <td style='padding:8px 0;text-align:right;'>%s</td></tr>"
                + "<tr><td style='padding:8px 0;color:%s;'>IVM</td>"
                + "    <td style='padding:8px 0;text-align:right;'>%s</td></tr>"
                + "<tr><td style='padding:8px 0;color:%s;'>Banco Popular trabajador</td>"
                + "    <td style='padding:8px 0;text-align:right;'>%s</td></tr>"
                + "<tr><td style='padding:8px 0;color:%s;'>Renta salarial</td>"
                + "    <td style='padding:8px 0;text-align:right;'>%s</td></tr>"
                + "<tr><td style='padding:8px 0;color:%s;'><b>Total deducciones</b></td>"
                + "    <td style='padding:8px 0;text-align:right;'><b>%s</b></td></tr>"
                + "<tr><td style='padding:8px 0;color:%s;'><b>Salario neto</b></td>"
                + "    <td style='padding:8px 0;text-align:right;color:%s;'><b>%s</b></td></tr>"
                + "</table>"
                + "<h3 style='margin:18px 0 8px 0;color:%s;'>Resumen patronal</h3>"
                + "<table style='width:100%%;border-collapse:collapse;font-size:13px;'>"
                + "<tr><td style='padding:8px 0;color:%s;'>Aportes patronales</td>"
                + "    <td style='padding:8px 0;text-align:right;'>%s</td></tr>"
                + "<tr><td style='padding:8px 0;color:%s;'>Costo total empresa</td>"
                + "    <td style='padding:8px 0;text-align:right;'>%s</td></tr>"
                + "</table>"
                + "<p style='margin-top:18px;color:%s;'><b>PDF:</b> %s</p>"
                + "</body></html>").formatted(
                        cTexto, cFondo,
                        cPrimario, nomina.getNombreEmpleado(),
                        cSuave, FormatoUtil.formatearPeriodo(nomina.getPeriodo()),
                        FormatoUtil.formatearFecha(nomina.getFechaGeneracion()),
                        cSuave, FormatoUtil.formatearMoneda(nomina.getSalarioBruto()),
                        cSuave, FormatoUtil.formatearMoneda(nomina.getDeduccionSem()),
                        cSuave, FormatoUtil.formatearMoneda(nomina.getDeduccionIvm()),
                        cSuave, FormatoUtil.formatearMoneda(nomina.getDeduccionBancoPopular()),
                        cSuave, FormatoUtil.formatearMoneda(nomina.getDeduccionRenta()),
                        cSuave, FormatoUtil.formatearMoneda(nomina.getTotalDeducciones()),
                        cPrimario, cPrimario, FormatoUtil.formatearMoneda(nomina.getSalarioNeto()),
                        cPrimario,
                        cSuave, FormatoUtil.formatearMoneda(nomina.getTotalAportesPatronales()),
                        cSuave, FormatoUtil.formatearMoneda(nomina.getCostoTotalEmpresa()),
                        cSuave, nomina.getRutaPdf() == null || nomina.getRutaPdf().isBlank()
                                ? "Sin generar" : nomina.getRutaPdf()));
        panelDetalle.setCaretPosition(0);
    }

    public void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel crearResumenSuperior() {
        panelResumenCards = new JPanel(new GridLayout(1, 4, 14, 0));
        panelResumenCards.setOpaque(false);
        panelResumenCards.add(tarjetaRegistros);
        panelResumenCards.add(tarjetaBruto);
        panelResumenCards.add(tarjetaNeto);
        panelResumenCards.add(tarjetaCosto);

        btnToggleResumen = new JButton("-");
        TemaVisual.estilizarBotonSecundario(btnToggleResumen);
        btnToggleResumen.setFont(TemaVisual.fuente(Font.PLAIN, 11));
        btnToggleResumen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaVisual.BORDE, 1, true),
                BorderFactory.createEmptyBorder(2, 7, 2, 7)));
        btnToggleResumen.setPreferredSize(new Dimension(28, 22));
        btnToggleResumen.setMaximumSize(new Dimension(28, 22));
        btnToggleResumen.setToolTipText("Minimizar resumen");
        btnToggleResumen.addActionListener(e -> toggleResumen());

        JLabel lbl = new JLabel("Resumen general");
        lbl.setFont(TemaVisual.fuente(Font.PLAIN, 12));
        lbl.setForeground(TemaVisual.TEXTO_SUAVE);

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.add(lbl, BorderLayout.WEST);
        cabecera.add(btnToggleResumen, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout(0, 8));
        wrapper.setOpaque(false);
        wrapper.add(cabecera, BorderLayout.NORTH);
        wrapper.add(panelResumenCards, BorderLayout.CENTER);
        return wrapper;
    }

    private void toggleResumen() {
        boolean visible = panelResumenCards.isVisible();
        panelResumenCards.setVisible(!visible);
        btnToggleResumen.setText(visible ? "+" : "-");
        btnToggleResumen.setToolTipText(visible ? "Expandir resumen" : "Minimizar resumen");
        revalidate();
        repaint();
    }

    private JComponent crearTarjetaGeneracion() {
        PanelRedondeado tarjeta = new PanelRedondeado(TemaVisual.SUPERFICIE, 26);
        tarjeta.setLayout(new BorderLayout(0, 18));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.add(TemaVisual.crearTituloSeccion("Generación de nómina"));
        encabezado.add(Box.createVerticalStrut(6));
        encabezado.add(TemaVisual.crearSubtitulo("Prepare el período, seleccione el colaborador y ejecute la liquidación."));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        formulario.add(crearBloqueCampo("Empleado activo", cmbEmpleados), gbc);

        gbc.gridy++;
        formulario.add(crearBloqueCampo("Período", spPeriodo), gbc);

        gbc.gridy++;
        formulario.add(chkEnviarAutomatico, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(6, 0, 10, 0);
        formulario.add(crearBloqueCampo("Correo del patrono", txtCorreoPatrono), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        formulario.add(chkEnviarPatrono, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(4, 0, 4, 0);
        formulario.add(lblEstadoGeneracion, gbc);

        JPanel subAcciones = new JPanel(new GridLayout(3, 2, 10, 10));
        subAcciones.setOpaque(false);
        subAcciones.add(btnExportarSeleccionada);
        subAcciones.add(btnExportarGeneral);
        subAcciones.add(btnEnviarCorreo);
        subAcciones.add(btnEnviarCorreoPatrono);
        subAcciones.add(btnRecargar);
        JPanel relleno = new JPanel();
        relleno.setOpaque(false);
        subAcciones.add(relleno);

        JPanel acciones = new JPanel(new BorderLayout(0, 10));
        acciones.setOpaque(false);
        acciones.add(btnGenerar, BorderLayout.NORTH);
        acciones.add(subAcciones, BorderLayout.CENTER);

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
        encabezado.add(TemaVisual.crearSubtitulo("Visualice el impacto de la nómina seleccionada antes de exportar o enviar."));

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
        textos.add(TemaVisual.crearTituloSeccion("Historial de nóminas"));
        textos.add(Box.createVerticalStrut(6));
        lblResumenPeriodo = new JLabel("Cargando historial...");
        lblResumenPeriodo.setFont(TemaVisual.fuente(Font.PLAIN, 13));
        lblResumenPeriodo.setForeground(TemaVisual.TEXTO_SUAVE);
        textos.add(lblResumenPeriodo);
        encabezado.add(textos, BorderLayout.WEST);

        JPanel ayuda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        ayuda.setOpaque(false);
        ayuda.add(btnEliminar);
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
