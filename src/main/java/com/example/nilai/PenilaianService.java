package com.example.nilai;

/**
 * Layanan sederhana untuk validasi, perhitungan, dan klasifikasi nilai mahasiswa.
 * Semua method berisi logika bersyarat sehingga cocok untuk latihan unit test.
 */
public class PenilaianService {

    public static final int NILAI_MIN = 0;
    public static final int NILAI_MAKS = 100;
    public static final int BATAS_LULUS = 55;
    public static final int BATAS_KEHADIRAN = 75;
    public static final int PENALTI_KEHADIRAN = 10;

    /**
     * Klasifikasi nilai angka menjadi nilai huruf.
     * A: 85-100, B: 70-84, C: 55-69, D: 40-54, E: 0-39.
     *
     * @throws IllegalArgumentException jika nilai di luar rentang 0-100
     */
    public String klasifikasiNilai(int nilai) {
        validasiRentang(nilai, "Nilai");
        if (nilai >= 85) {
            return "A";
        } else if (nilai >= 70) {
            return "B";
        } else if (nilai >= 55) {
            return "C";
        } else if (nilai >= 40) {
            return "D";
        }
        return "E";
    }

    /**
     * Mahasiswa lulus jika nilai >= 55.
     *
     * @throws IllegalArgumentException jika nilai di luar rentang 0-100
     */
    public boolean isLulus(int nilai) {
        validasiRentang(nilai, "Nilai");
        return nilai >= BATAS_LULUS;
    }

    /**
     * Nilai akhir = 30% tugas + 30% UTS + 40% UAS.
     *
     * @throws IllegalArgumentException jika salah satu komponen di luar rentang 0-100
     */
    public double hitungNilaiAkhir(double tugas, double uts, double uas) {
        validasiRentang(tugas, "Nilai tugas");
        validasiRentang(uts, "Nilai UTS");
        validasiRentang(uas, "Nilai UAS");
        return (tugas * 0.30) + (uts * 0.30) + (uas * 0.40);
    }

    /**
     * Penalti kehadiran: jika kehadiran < 75% maka nilai dikurangi 10 (minimum 0).
     *
     * @throws IllegalArgumentException jika nilai atau persentase kehadiran di luar 0-100
     */
    public int terapkanPenaltiKehadiran(int nilai, int persenKehadiran) {
        validasiRentang(nilai, "Nilai");
        validasiRentang(persenKehadiran, "Persentase kehadiran");
        if (persenKehadiran < BATAS_KEHADIRAN) {
            return Math.max(NILAI_MIN, nilai - PENALTI_KEHADIRAN);
        }
        return nilai;
    }

    private void validasiRentang(double nilai, String namaField) {
        if (nilai < NILAI_MIN || nilai > NILAI_MAKS) {
            throw new IllegalArgumentException(
                    namaField + " harus berada pada rentang 0-100, tetapi: " + nilai);
        }
    }
}
