package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tassty.R
import com.example.tassty.model.Review
import com.example.tassty.reviews
import com.example.tassty.ui.theme.Neutral10 // Asumsi Neutral10 adalah warna latar belakang card (putih)
import com.example.tassty.ui.theme.Neutral100 // Asumsi Neutral100 adalah warna teks utama (hitam)
import com.example.tassty.ui.theme.Neutral70 // Asumsi Neutral70 adalah warna teks sekunder (abu-abu)
import com.example.tassty.ui.theme.Orange500 // Asumsi Orange500 adalah warna bintang
import com.example.tassty.ui.theme.LocalCustomTypography // Asumsi untuk tipografi kustom
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Pink500
import kotlin.contracts.contract

@Composable
fun ReviewCard(
    review: Review,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral10), // Background putih
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Bagian Profil dan Tanggal
            Row {
                CommonImage(
                    imageUrl = "",
                    name= "Profile picture of ${review.userName}",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Column(
                    modifier = Modifier.padding(start = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Bagian Bintang (Rating)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Tampilkan bintang sesuai rating
                        repeat(review.rating) {
                            Icon(
                                painter = painterResource(R.drawable.star),
                                contentDescription = null,
                                tint = Orange500, // Warna emas/oranye untuk bintang
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    // Nama Pengguna dan Tanggal
                    Row (horizontalArrangement = Arrangement.spacedBy(4.dp)){
                        Text(
                            text = review.userName,
                            style = LocalCustomTypography.current.h8Bold, // Asumsi bodyMediumBold
                            color = Neutral100
                        )
                        Text(
                            text = " • ",
                            style = LocalCustomTypography.current.bodyTinyMedium, // Asumsi bodyMediumMedium
                            color = Neutral70
                        )

                        Text(
                            text = review.date,
                            style = LocalCustomTypography.current.bodyTinyMedium, // Asumsi bodyMediumMedium
                            color = Neutral70
                        )
                    }

                    Spacer(Modifier.height(4.dp)) // Jarak antara profil dan teks ulasan

                    // Teks Ulasan
                    Text(
                        text = review.reviewText,
                        style = LocalCustomTypography.current.bodyXtraSmallRegular,
                        color = Neutral70,
                        lineHeight = 16.sp
                    )
                }
            }

        }
    }
}


@Composable
fun ReviewLargeCard(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ... (Bagian Header Ulasan tetap sama) ...
        Row(modifier = Modifier
            .fillMaxWidth()) {
            // Gambar Profil (menggunakan placeholder)
            CommonImage(
                imageUrl = "",
                name = "profile reviewers",
                modifier = Modifier.size(44.dp).clip(CircleShape)
            )

            Column(
                modifier = Modifier.padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Bagian Bintang (Rating)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Tampilkan bintang sesuai rating
                    repeat(review.rating) {
                        Icon(
                            painter = painterResource(R.drawable.star),
                            contentDescription = "review stars",
                            tint = Orange500, // Warna emas/oranye untuk bintang
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Nama Pengguna dan Tanggal
                Row (horizontalArrangement = Arrangement.spacedBy(4.dp)){
                    Text(
                        text = review.userName,
                        style = LocalCustomTypography.current.h5Bold, // Asumsi bodyMediumBold
                        color = Neutral100
                    )
                    Text(
                        text = " • ",
                        style = LocalCustomTypography.current.bodySmallMedium, // Asumsi bodyMediumMedium
                        color = Neutral70
                    )

                    Text(
                        text = review.date,
                        style = LocalCustomTypography.current.bodySmallMedium, // Asumsi bodyMediumMedium
                        color = Neutral70
                    )
                }
            }
        }

        // 🟢 Area tempat Segitiga akan muncul (sekitar foto profil)
        val triangleOffset = 16.dp // (Setengah tinggi foto 44.dp) + (Padding Kiri 16.dp)

        // 🟢 Menggunakan Box dengan Padding untuk menempatkan Segitiga
        Box(modifier = Modifier.fillMaxWidth()
        ) {
            // Konten Ulasan (Kotak Abu-abu Utama)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    // 🟢 PENTING: Menggambar Segitiga di belakang konten
                    .drawBehind {
                        drawBubbleTail(
                            // Posisi horizontal di mana segitiga menunjuk
                            xOffset = triangleOffset.toPx(),
                            yOffset = 0f,
                            color = Neutral20 // Warna yang sama dengan latar belakang kotak
                        )
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(Neutral20)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Indah Café offers a fantastic dining experience with a warm ambiance and friendly staff. It's the perfect place for a special occasion or simply to enjoy a great meal with loved ones. I look forward to my next visit!",
                    style = LocalCustomTypography.current.bodySmallRegular,
                    color = Neutral70
                )
            }
        }

        // ... (Bagian Order tetap sama) ...
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)){
            Icon(
                painter = painterResource(R.drawable.clipboard_list),
                tint = Pink500,
                contentDescription = "order review"
            )
            Text(
                text = "Order: ",
                style = LocalCustomTypography.current.bodyXtraSmallSemiBold,
                color = Neutral100
            )
            Text(
                text = "Salad, Steak, etc",
                style = LocalCustomTypography.current.bodyXtraSmallMedium,
                color = Neutral70
            )
        }
    }
}

// 2. Fungsi untuk Menggambar Segitiga Kecil (Bubble Tail)
fun DrawScope.drawBubbleTail(xOffset: Float, yOffset: Float, color: Color) {
    val tailWidth = 10.dp.toPx()
    val tailHeight = 8.dp.toPx()

    // Path untuk bentuk segitiga (menunjuk ke atas)
    val path = Path().apply {
        // Mulai dari sudut kanan bawah segitiga
        moveTo(xOffset + tailWidth / 2f, yOffset)
        // Pindah ke puncak (ujung) segitiga
        lineTo(xOffset, yOffset - tailHeight) // Naik ke puncak
        // Pindah ke sudut kiri bawah segitiga
        lineTo(xOffset - tailWidth / 2f, yOffset)
        // Tutup jalur
        close()
    }

    // Gambar segitiga
    drawPath(path, color = color)
}


@Preview(showBackground = true)
@Composable
fun PreviewReviewCard() {
    Column(modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReviewCard(review = reviews[0])
    }
}
