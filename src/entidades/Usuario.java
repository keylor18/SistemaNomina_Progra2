package entidades;

/**
 * Usuario autorizado para ingresar al sistema.
 */
public class Usuario {

    private String username;
    private String nombreCompleto;
    private String passwordHash;
    private RolUsuario rol;
    private int intentosFallidos;
    private boolean bloqueado;

    public Usuario() {
    }

    public Usuario(String username, String nombreCompleto, String passwordHash, RolUsuario rol,
            int intentosFallidos, boolean bloqueado) {
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.intentosFallidos = intentosFallidos;
        this.bloqueado = bloqueado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }
}
