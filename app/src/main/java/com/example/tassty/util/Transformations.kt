package com.example.tassty.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

object Transformations {
    class CardNumber : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
            var out = ""
            for (i in trimmed.indices) {
                out += trimmed[i]
                if (i % 4 == 3 && i != 15) out += "-"
            }

            val offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return when {
                        offset <= 3 -> offset
                        offset <= 7 -> offset + 1
                        offset <= 11 -> offset + 2
                        else -> offset + 3
                    }
                }
                override fun transformedToOriginal(offset: Int): Int {
                    return when {
                        offset <= 4 -> offset
                        offset <= 9 -> offset - 1
                        offset <= 14 -> offset - 2
                        else -> offset - 3
                    }
                }
            }
            return TransformedText(AnnotatedString(out), offsetMapping)
        }
    }

    // Expiry Date (MM/YY)
    class ExpiryDateTransformation : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
            var out = ""

            for (i in trimmed.indices) {
                out += trimmed[i]
                // Tambah slash HANYA setelah karakter ke-2 (index 1)
                // DAN jika masih ada karakter setelahnya
                if (i == 1 && trimmed.length > 2) out += "/"
            }

            val offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    // Jika sudah melewati angka ke-2, posisi geser +1 karena ada slash
                    if (offset <= 2) return offset
                    return offset + 1
                }

                override fun transformedToOriginal(offset: Int): Int {
                    // Jika di tampilan (transformed) sudah melewati posisi ke-3 (angka + slash)
                    if (offset <= 2) return offset
                    return offset - 1
                }
            }

            return TransformedText(AnnotatedString(out), offsetMapping)
        }
    }

    class PhoneNumberTransformation : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            // Misal kita batasi maksimal 13 angka (standar HP Indonesia)
            val trimmed = if (text.text.length >= 13) text.text.substring(0..12) else text.text

            var out = ""
            for (i in trimmed.indices) {
                out += trimmed[i]
                // Tambah "-" setelah digit ke-4 dan ke-8
                if (i == 3 || i == 7) {
                    if (i != trimmed.lastIndex) out += "-"
                }
            }

            val offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return when {
                        offset <= 4 -> offset
                        offset <= 8 -> offset + 1
                        else -> offset + 2
                    }
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return when {
                        offset <= 4 -> offset
                        offset <= 9 -> offset - 1 // offset 9 adalah posisi setelah dash pertama
                        else -> offset - 2
                    }
                }
            }

            return TransformedText(AnnotatedString(out), offsetMapping)
        }
    }
}
