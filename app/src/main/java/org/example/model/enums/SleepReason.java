package org.example.model.enums; // Atau package yang sesuai

public enum SleepReason {
    NOT_SLEEPING,       // Kondisi default saat pemain bangun dan aktif
    NORMAL,             // Pemain tidur secara normal (misalnya, melalui interaksi dengan tempat tidur)
    PASSED_OUT_ENERGY,  // Pemain pingsan karena energi habis
    PASSED_OUT_TIME     // Pemain pingsan karena sudah terlalu larut (jam 02:00)
}