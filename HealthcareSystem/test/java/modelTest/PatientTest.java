package modelTest;

import model.Patient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;

import junitparams.JUnitParamsRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnitParamsRunner.class)
public class PatientTest {

    Patient firstEqualPatient;

    Patient secondEqualPatient;

    List<Patient> patients;

    @BeforeEach
    public  void setUp() {
        this.firstEqualPatient = new Patient("Martin Brown", 23, 'M',
                "A+", false, "None", List.of("Flu", "COVID-19"));
        this.secondEqualPatient = new Patient("Martin Brown", 23, 'M',
                "A+", false, "None", List.of("Flu", "COVID-19"));
        patients = new ArrayList<>();
        patients.add(new Patient("Michael Metzer", 1979, 'M',
                                "AB+", false, "Chord", List.of("Flu")));
        patients.add(new Patient("John Mund", 1996, 'M',
                                "A-", false, "High Blood Pressure",
                                          List.of("COVID-19", "Tetanus", "Hepatitis B")));
        patients.add(new Patient("Teddy Wallen", 2009, 'M',
                                "O+", true, "None", List.of("Influenza", "HPV")));
        patients.add(new Patient("Selena Caylor", 1985, 'F',
                                "B+", false, "Heart Disease",
                                          List.of("Measles", "Hepatitis A", "Varicella")));
        patients.add(new Patient("Leyla Heilig", 2002, 'F',
                                "AB+", false, "High Blood Pressure", List.of()));
    }



    @Test
    public void newPatientTest() {
        Patient patient = new Patient();
        patient.setFullName("Martin");
        Assertions.assertEquals("Martin", patient.getFullName());
    }

    @Test
    public void patientEqualTest() {
        Assertions.assertEquals(this.firstEqualPatient, this.secondEqualPatient);
    }

    @Test
    public void patientHashCodeTest() {
        Assertions.assertEquals(this.firstEqualPatient, this.secondEqualPatient);
    }

    @Test
    public void addVaccinationTest() {
        firstEqualPatient.addVaccination("Hepatitis B");
        Assertions.assertLinesMatch(List.of("Flu", "COVID-19", "Hepatitis B"), firstEqualPatient.getVaccinations());
    }

    @Test
    public void delVaccinationTest() {
        firstEqualPatient.delVaccination("Flu");
        Assertions.assertLinesMatch(List.of("COVID-19"), firstEqualPatient.getVaccinations());
    }

    @Test
    public void findByFullNameSuccessTest() {
        Assertions.assertEquals(3, Patient.findByFullName(patients, "Le").size());
    }

    @Test
    public void findByFullNameUnSuccessTest() {
        Assertions.assertEquals(0, Patient.findByFullName(patients, "Tom").size());
    }

    @Test
    public void findByFullNameBlankTest() {
        Assertions.assertEquals(5, Patient.findByFullName(patients, "").size());
    }

    @Test
    public void findByBloodTypeSuccessTest() {
        Assertions.assertEquals(3, Patient.findByBloodType(patients, "A").size());
    }

    @Test
    public void findByBloodTypeUnSuccessTest() {
        Assertions.assertEquals(0, Patient.findByBloodType(patients, "O-").size());
    }

    @Test
    public void findByBloodTypeBlankTest() {
        Assertions.assertEquals(5, Patient.findByBloodType(patients, "").size());
    }

    @Test
    public void findByChrDiseaseSuccessTest() {
        Assertions.assertEquals(2, Patient.findByChrDisease(patients, "High Blood Pressure").size());
    }

    @Test
    public void findByChrDiseaseUnSuccessTest() {
        Assertions.assertEquals(0, Patient.findByChrDisease(patients, "High Cholesterol").size());
    }

    @Test
    public void findByChrDiseaseBlankTest() {
        Assertions.assertEquals(5, Patient.findByChrDisease(patients, "").size());
    }

    @Test
    public void findByVaccinationsSuccessTest() {
        Assertions.assertEquals(1, Patient.findByVaccination(patients, "COVID-19").size());
    }

    @Test
    public void findByVaccinationsUnSuccessTest() {
        Assertions.assertEquals(0, Patient.findByVaccination(patients, "Hepatitis C").size());
    }

    @Test
    public void findByVaccinationsBlankTest() {
        Assertions.assertEquals(5, Patient.findByVaccination(patients, "").size());
    }

    private static Object[] findMaxAgeCrtBTypeSuccessParams() {
        return new Object[] {
                new Object[] {-1, "O+", 0},
                new Object[] {0, "", 0},
                new Object[] {10, " \n", 0},
                new Object[] {10, "AB+", 0},
                new Object[] {25, "AB+", 1},
                new Object[] {45, "AB+", 2}
        };
    }

    // Використати Parametrized tests
    @Test
    public void findMaxAgeCrtBTypeUnSuccessTest() {
        Assertions.assertEquals(0, Patient.findMaxAgeCrtBType(patients, 35, "B+").size());
    }

    @Test
    public void findMaxAgeMinVaccTest() {
        Assertions.assertEquals(1, Patient.findMaxAgeMinVacc(patients).size());
    }

    @Test
    public void findMaxVaccNoDsblTest() {
        Assertions.assertEquals(2, Patient.findMaxVaccNoDsbl(patients).size());
    }
}
