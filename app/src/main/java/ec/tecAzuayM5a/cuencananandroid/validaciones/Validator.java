package ec.tecAzuayM5a.cuencananandroid.validaciones;

public class Validator {

    // Método para validar el formato del email
    public static boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$";
        return email.matches(emailPattern);
    }

    // Método para validar el formato de la contraseña
    public static boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(passwordPattern);
    }

    // Método para validar el formato de la cédula
    public static boolean isValidCedula(String cedula) {
        String cedulaPattern = "^\\d{10}$";
        return cedula.matches(cedulaPattern);
    }

    // Método para validar el formato del número de celular
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phonePattern = "^\\d{10}$";
        return phoneNumber.matches(phonePattern);
    }

    // Método para validar el formato de nombres y apellidos
    public static boolean isValidName(String name) {
        String namePattern = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+$";
        return name.matches(namePattern);
    }
}
