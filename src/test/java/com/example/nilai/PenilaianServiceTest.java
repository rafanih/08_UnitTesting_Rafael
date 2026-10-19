package com.example.nilai;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("PenilaianService")
class PenilaianServiceTest {

    private PenilaianService service;

    // FIRST - Independent/Isolated: setiap test mendapat instance baru
    @BeforeEach
    void setUp() {
        service = new PenilaianService();
    }

    // ---------------------------------------------------------------
    // 1. Klasifikasi nilai - Boundary Value Analysis (parameterized)
    // ---------------------------------------------------------------
    @ParameterizedTest(name = "[{index}] nilai {0} -> huruf {1}")
    @DisplayName("klasifikasiNilai: BVA pada setiap batas kelas")
    @CsvSource({
            "0,   E",   // batas bawah rentang valid
            "39,  E",   // batas atas kelas E
            "40,  D",   // batas bawah kelas D
            "54,  D",   // batas atas kelas D
            "55,  C",   // batas bawah kelas C
            "69,  C",   // batas atas kelas C
            "70,  B",   // batas bawah kelas B
            "84,  B",   // batas atas kelas B
            "85,  A",   // batas bawah kelas A
            "100, A"    // batas atas rentang valid
    })
    void klasifikasiNilai_batasKelas_mengembalikanHurufBenar(int nilai, String hurufDiharapkan) {
        assertEquals(hurufDiharapkan, service.klasifikasiNilai(nilai));
    }

    @Test
    @DisplayName("klasifikasiNilai: nilai -1 (di bawah batas valid) melempar IllegalArgumentException")
    void klasifikasiNilai_dibawahNol_melemparException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.klasifikasiNilai(-1));

        assertTrue(ex.getMessage().contains("0-100"));
    }

    @Test
    @DisplayName("klasifikasiNilai: nilai 101 (di atas batas valid) melempar IllegalArgumentException")
    void klasifikasiNilai_diatasSeratus_melemparException() {
        assertThrows(IllegalArgumentException.class, () -> service.klasifikasiNilai(101));
    }

    // ---------------------------------------------------------------
    // 2. Status kelulusan
    // ---------------------------------------------------------------
    @Test
    @DisplayName("isLulus: nilai 55 (tepat batas lulus) -> true")
    void isLulus_tepatDiBatas_true() {
        assertTrue(service.isLulus(55));
    }

    @Test
    @DisplayName("isLulus: nilai 54 (tepat di bawah batas lulus) -> false")
    void isLulus_satuDibawahBatas_false() {
        assertFalse(service.isLulus(54));
    }

    // ---------------------------------------------------------------
    // 3. Perhitungan nilai akhir
    // ---------------------------------------------------------------
    @Test
    @DisplayName("hitungNilaiAkhir: bobot 30/30/40 menghasilkan nilai akhir yang benar")
    void hitungNilaiAkhir_komponenValid_hasilBerbobot() {
        // 80*0.3 + 70*0.3 + 90*0.4 = 24 + 21 + 36 = 81
        double hasil = service.hitungNilaiAkhir(80, 70, 90);

        assertEquals(81.0, hasil, 0.001);
    }

    @Test
    @DisplayName("hitungNilaiAkhir: komponen di luar rentang melempar exception dengan pesan field")
    void hitungNilaiAkhir_komponenTidakValid_melemparException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.hitungNilaiAkhir(80, 70, 100.5));

        assertTrue(ex.getMessage().contains("UAS"));
    }

    // ---------------------------------------------------------------
    // 4. Penalti kehadiran
    // ---------------------------------------------------------------
    @Test
    @DisplayName("terapkanPenaltiKehadiran: kehadiran 74% -> nilai dikurangi 10")
    void penaltiKehadiran_dibawahBatas_nilaiBerkurang() {
        assertEquals(70, service.terapkanPenaltiKehadiran(80, 74));
    }

    @Test
    @DisplayName("terapkanPenaltiKehadiran: kehadiran tepat 75% -> tanpa penalti")
    void penaltiKehadiran_tepatDiBatas_tanpaPenalti() {
        assertEquals(80, service.terapkanPenaltiKehadiran(80, 75));
    }

    @Test
    @DisplayName("terapkanPenaltiKehadiran: hasil tidak pernah di bawah 0")
    void penaltiKehadiran_nilaiKecil_tidakNegatif() {
        assertAll(
                () -> assertEquals(0, service.terapkanPenaltiKehadiran(5, 50)),
                () -> assertEquals(0, service.terapkanPenaltiKehadiran(0, 0)));
    }

    @Test
    @DisplayName("terapkanPenaltiKehadiran: persentase kehadiran 101 melempar exception")
    void penaltiKehadiran_kehadiranTidakValid_melemparException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.terapkanPenaltiKehadiran(80, 101));
    }
}
