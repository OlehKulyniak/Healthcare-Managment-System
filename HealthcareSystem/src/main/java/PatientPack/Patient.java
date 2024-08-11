package PatientPack;

//Інтерфейс для власної реалізації серіалізації
import java.io.Externalizable;
//Об'єкти для власної реалізації серіалізації
import java.io.ObjectOutput;
import java.io.ObjectInput;
//Exception, який кидають об'єкти серіалзіації
import java.io.IOException;
// List - це інтерфейс, що визначає основні функції для його реалізацій: ArrayList, LinkedList, застарілі Vector, Stack.
import java.util.List;
// Реалізація List, масив елементів з можливістю додавати та видаляти елементи.
// Забезпечує RandomAccess доступ до елементів.
// За своїм функціоналом схожий до std::vector з C++
import java.util.ArrayList;
// Клас, що використовується для представлення року
import java.time.Year;
public class Patient implements Externalizable {
    private String fullName;
    private int birthYear;
    private char gender;
    private String bloodType;
    private boolean isDisabled;
    private String chrDisease;

    // Використовується List для зручного додавання та видалення елементів.
    // Оскільки List це інтерфейс, то для створення об'єкта потрібно використати його реалізацію ArrayList або LinkedList.
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

    // Оскільки в java немає прямого конструктора переміщення.
    // Для його реалізації використовується додатковий boolean параметр для вказання, що це конструктор переміщення.
    /**
     * Конструктор переміщення.
     */
    public Patient(Patient otherPatient, boolean moveMarker) {
        this.fullName = otherPatient.fullName;
        this.birthYear = otherPatient.birthYear;
        this.gender = otherPatient.gender;
        this.bloodType = otherPatient.bloodType;
        this.isDisabled = otherPatient.isDisabled;
        this.chrDisease = otherPatient.chrDisease;
        this.vaccinations = otherPatient.vaccinations;

        otherPatient.fullName = null;
        otherPatient.birthYear = 0;
        otherPatient.gender = '\0';
        otherPatient.bloodType = null;
        otherPatient.isDisabled = false;
        otherPatient.chrDisease = null;
        otherPatient.vaccinations = null;
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
    public int delVaccination(String vaccination) {
        if(vaccinations.remove(vaccination)) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Функція пошуку пацієнтів зі списку patients за іменем. <br>
     * Функція приймає ім'я fullName та виконує пошук входження fullName у fullName кожного елемента списку patients.<br>
     * Функція повертає List, якщо відбувся пошук, або null, якщо patients == null.
     */
    public static List<Patient> findByFullName(List<Patient> patients, String fullName) {
        if(patients == null) {
            return null;
        }
        if(fullName.isEmpty() || fullName.isBlank()) {
            return new ArrayList<>();
        }
        // Представлення роботи StreamAPI функції filter.
        List<Patient> foundPatientsList = new ArrayList<>();
        for(Patient patient : patients) {
            if(patient.getFullName().contains(fullName)) {
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
        if(patients == null) {
            return null;
        }
        if(bloodType.isEmpty() || bloodType.isBlank()) {
            return new ArrayList<>();
        }
        // Використання StreamAPI функції filter для пошуку елементів, що містять входження bloodType.
        // Представлення функцією використовується в методі findByFullName.
        return patients.stream().filter(elem -> elem.getBloodType().contains(bloodType)).toList();
    }

    /**
     * Функція пошуку пацієнтів зі списку patients за хронічним захворюванням.<br>
     * Функція приймає chrDisease та виконує пошук входження chrDisease в chrDisease елементів списку patients.<br>
     * Функція повертає List, якщо відбувся пошук, або null, якщо patients == null.
     */
    public static List<Patient> findByChrDisease(List<Patient> patients, String chrDisease) {
        if(patients == null) {
            return null;
        }
        if(chrDisease.isEmpty() || chrDisease.isBlank()) {
            return new ArrayList<>();
        }
        // Використання StreamAPI функції filter для пошуку елементів, що містять входження chrDisease.
        // Представлення функцією використовується в методі findByFullName.
        return patients.stream().filter(elem -> elem.getChrDisease().contains(chrDisease)).toList();
    }

    /**
     * Функція пошуку пацієнтів зі списку patients за щепленням. <br>
     * Функція приймає vaccination та виконує пошук входження vaccination у масиві vaccinations елементів списку patients.
     * <br>Функція повертає List, якщо відбувся пошук, або null, якщо patients == null.
     * */
    public static List<Patient> findByVaccination(List<Patient> patients, String vaccination) {
        if(patients == null) {
            return null;
        }
        if(vaccination.isEmpty() || vaccination.isBlank()) {
            return new ArrayList<>();
        }
        List<Patient> foundPatientsList = new ArrayList<>();
        for(Patient patient : patients) {
            for(String currVaccination : patient.getVaccinations()) {
                if(currVaccination.contains(vaccination)) {
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
        if(patients == null) {
            return null;
        }
        if(age <= 0 || bloodType.isEmpty() || bloodType.isBlank()) {
            return new ArrayList<>();
        }
        int currYear = Year.now().getValue();
        // StreamAPI функція filter для пошуку елементів молодших ніж age та з заданою групою крові.
        return patients.stream().filter(elem -> (currYear - elem.getBirthYear() ) < age &&
                                                            elem.getBloodType().equals(bloodType)).toList();
    }

    /**
     * Функція пошуку найстарших пацієнтів з найменшою кількістю щеплень. <br>
     * Функція шукає середній вік пацієнтів і мінімальну кількість щеплень. <br>
     * Після цього виконується пошук елементів, старших за середній вік і з найменшою кількістю щеплень. <br>
     * Функція повертає List, якщо пошук був успішним, або null, якщо patients == null.
     * */
    public static List<Patient> findMaxAgeMinVacc(List<Patient> patients) {
        if(patients == null) {
            return null;
        }
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
        if(patients == null) {
            return null;
        }
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