package model;

import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.time.Year;
import java.util.Objects;

public class Patient implements Externalizable {
    private String fullName;
    private int birthYear;
    private char gender;
    private String bloodType;
    private boolean isDisabled;
    private String chrDisease;

    private List<String> vaccinations;

    /**
     * Конструктор за замовчуванням.
     */
    public Patient() {
        vaccinations = new ArrayList<>();
    }

    /**
     * Конструктор з параметрами.
     */
    public Patient(String fullName, int birthYear, char gender, String bloodType, boolean isDisabled,
                   String chrDisease, List<String> vaccinations) {
        this.fullName = fullName;
        this.birthYear = birthYear;
        this.gender = gender;
        this.bloodType = bloodType;
        this.isDisabled = isDisabled;
        this.chrDisease = chrDisease;
        this.vaccinations = new ArrayList<>(vaccinations);
    }

    /**
     * Конструктор копій.
     */
    public Patient(Patient otherPatient) {
        this.fullName = otherPatient.fullName;
        this.birthYear = otherPatient.birthYear;
        this.gender = otherPatient.gender;
        this.bloodType = otherPatient.bloodType;
        this.isDisabled = otherPatient.isDisabled;
        this.chrDisease = otherPatient.chrDisease;
        this.vaccinations = new ArrayList<>(otherPatient.vaccinations);
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public int getBirthYear() {
        return birthYear;
    }
    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }
    public char getGender() {
        return gender;
    }
    public void setGender(char gender) {
        this.gender = gender;
    }
    public String getBloodType() {
        return bloodType;
    }
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    public boolean getIsDisabled() {
        return isDisabled;
    }
    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
    public String getChrDisease() {
        return chrDisease;
    }
    public void setChrDisease(String chrDisease) {
        this.chrDisease = chrDisease;
    }
    public List<String> getVaccinations() {
        return vaccinations;
    }
    public void setVaccinations(List<String> vaccinations) {
        this.vaccinations = vaccinations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fullName, this.birthYear, this.gender,
                                   this.bloodType, this.isDisabled, this.chrDisease, this.vaccinations);
    }

    @Override
    public boolean equals(Object other) {
    if(this == other) { return true; }
    if(other == null || this.getClass() != other.getClass()) {
        return false;
    }

    Patient otherPatient = (Patient) other;
    return this.fullName.equals(otherPatient.fullName) &&
            this.birthYear == otherPatient.birthYear &&
            this.gender == otherPatient.gender &&
            this.bloodType.equals(otherPatient.bloodType) &&
            this.isDisabled == otherPatient.isDisabled &&
            this.chrDisease.equals(otherPatient.chrDisease) &&
            this.vaccinations.equals(otherPatient.vaccinations);

    }
    /*
    /**
     * Перевизначена функція toString для виводу елемента у файл.
     */
    /* noinspection
    @Override
    public String toString() {
        return fullName + "|"
                + birthYear + "|"
                + gender + "|"
                + bloodType + "|"
                + isDisabled + "|"
                + chrDisease + "|"
                + String.join(",", vaccinations) + "|";
    }*/

    /**
     * Функція для читання серіалізації об'єкта з файлу
     */
    @Override
    @SuppressWarnings("unchecked")
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        fullName = (String) in.readObject();
        birthYear = (Integer) in.readObject();
        gender = (Character) in.readObject();
        bloodType = (String) in.readObject();
        isDisabled = (Boolean) in.readObject();
        chrDisease = (String) in.readObject();
        vaccinations = (List<String>) in.readObject();
    }

    /**
     * Функція для запису серіалізації об'єкта у файл
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.fullName);
        out.writeObject(this.birthYear);
        out.writeObject(this.gender);
        out.writeObject(this.bloodType);
        out.writeObject(this.isDisabled);
        out.writeObject(this.chrDisease);
        out.writeObject(this.vaccinations);
    }


    /**
     * Функція додавання нового щеплення до списку щеплень пацієнта.
     */
    public void addVaccination(String vaccination) {
        vaccinations.add(vaccination);
    }

    /**
     * Функція видалення щеплення зі списку щеплень пацієнта. <br>
     * Повертає 0 у випадку успішного видалення, інакше -1.
     */
    public boolean delVaccination(String vaccination) {
        return vaccinations.remove(vaccination);
    }

    /**
     * Функція пошуку пацієнтів зі списку patients за іменем. <br>
     * Функція приймає ім'я fullName та виконує пошук входження fullName у fullName кожного елемента списку patients.<br>
     * Функція повертає List, якщо відбувся пошук, або null, якщо patients == null.
     */
    public static List<Patient> findByFullName(List<Patient> patients, String fullName) {
        if(fullName.isEmpty() || fullName.isBlank()) {
            return patients;
        }
        // Представлення роботи StreamAPI функції filter.
        List<Patient> foundPatientsList = new ArrayList<>();
        for(Patient patient : patients) {
            if(patient.getFullName().toLowerCase().contains(fullName.toLowerCase())) {
                foundPatientsList.add(patient);
            }
        }
        return foundPatientsList;
    }

    /**
     * Функція пошуку пацієнтів зі списку patients за групою крові.<br>
     * Функція приймає bloodType та виконує пощук входження bloodType в bloodType елементів списку patients.<br>
     * Функція повертає List, якщо відбувся пошук, або null, якщо patients == null.
     */
    public static List<Patient> findByBloodType(List<Patient> patients, String bloodType) {
        if(bloodType.isEmpty() || bloodType.isBlank()) {
            return patients;
        }

        return patients.stream().filter(elem -> elem.getBloodType().toLowerCase()
                                                                   .contains(bloodType.toLowerCase())).toList();
    }

    /**
     * Функція пошуку пацієнтів зі списку patients за хронічним захворюванням.<br>
     * Функція приймає chrDisease та виконує пошук входження chrDisease в chrDisease елементів списку patients.<br>
     * Функція повертає List, якщо відбувся пошук, або null, якщо patients == null.
     */
    public static List<Patient> findByChrDisease(List<Patient> patients, String chrDisease) {
        if(chrDisease.isEmpty() || chrDisease.isBlank()) {
            return patients;
        }

        return patients.stream().filter(elem -> elem.getChrDisease().toLowerCase()
                                                                    .contains(chrDisease.toLowerCase())).toList();
    }

    /**
     * Функція пошуку пацієнтів зі списку patients за щепленням. <br>
     * Функція приймає vaccination та виконує пошук входження vaccination у масиві vaccinations елементів списку patients.
     * <br>Функція повертає List, якщо відбувся пошук, або null, якщо patients == null.
     * */
    public static List<Patient> findByVaccination(List<Patient> patients, String vaccination) {
        if(vaccination.isEmpty() || vaccination.isBlank()) {
            return patients;
        }
        List<Patient> foundPatientsList = new ArrayList<>();
        for(Patient patient : patients) {
            for(String currVaccination : patient.getVaccinations()) {
                if(currVaccination.toLowerCase().contains(vaccination.toLowerCase())) {
                    foundPatientsList.add(patient);
                    break;
                }
            }
        }
        return foundPatientsList;
    }


    /**
     * Функція пошуку пацієнтів молодших за age та з заданою групою крові bloodType. <br>
     * Функція повертає List, якщо пошук відбувся, або null, якщо patients == null.
     */
    public static List<Patient> findMaxAgeCrtBType(List<Patient> patients, int age, String bloodType) {
        if(age < 0 || bloodType.isEmpty() || bloodType.isBlank()) {
            return new ArrayList<>();
        }
        int currYear = Year.now().getValue();

        return patients.stream().filter(elem -> (currYear - elem.getBirthYear() ) <= age &&
                                                            elem.getBloodType().equals(bloodType)).toList();
    }

    /**
     * Функція пошуку найстарших пацієнтів з найменшою кількістю щеплень. <br>
     * Функція шукає середній вік пацієнтів і мінімальну кількість щеплень. <br>
     * Після цього виконується пошук елементів, старших за середній вік і з найменшою кількістю щеплень. <br>
     * Функція повертає List, якщо пошук був успішним, або null, якщо patients == null.
     * */
    public static List<Patient> findMaxAgeMinVacc(List<Patient> patients) {
        // Пошук середнього віку пацієнтів.
        int avgBirthYear = 0;
        for(Patient patient : patients) {
            avgBirthYear += patient.getBirthYear();
        }
        // Пошук мінімальної кількості щеплень.
        avgBirthYear /= patients.size();
        int minVacc = Integer.MAX_VALUE;
        for(Patient patient : patients) {
            if(patient.getBirthYear() <= avgBirthYear && patient.getVaccinations().size() < minVacc)  {
                minVacc = patient.getVaccinations().size();
            }
        }
        // Представлення виконання StreamAPI функції filter.
        List<Patient> resultList = new ArrayList<>();
        for(Patient patient : patients) {
            if(patient.getBirthYear() <= avgBirthYear && patient.getVaccinations().size() == minVacc) {
                resultList.add(patient);
            }
        }
        return resultList;
    }

    /**
     * Функція пошуку пацієнтів з максимальною кількістю щеплень і без інвалідності. <br>
     * Функція повертає List, якщо пошук був успішним, або null, якщо пацієнтів == null.
     */
    public static List<Patient> findMaxVaccNoDsbl(List<Patient> patients) {
        int maxVacc = patients.get(0).getVaccinations().size();
        for(int i = 1; i < patients.size(); i++) {
            if(maxVacc < patients.get(i).getVaccinations().size() && !patients.get(i).getIsDisabled()) {
                maxVacc = patients.get(i).getVaccinations().size();
            }
        }
        List<Patient> resultList = new ArrayList<>();
        for(Patient patient : patients) {
            if(patient.getVaccinations().size() == maxVacc && !patient.getIsDisabled()) {
                resultList.add(patient);
            }
        }
        return resultList;
    }
}