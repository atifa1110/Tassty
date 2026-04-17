package com.example.core.utils

import com.example.core.domain.utils.DateTimeDisplay
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateFormatter {

    private val localeID = Locale.forLanguageTag("id-ID")

    private val timeFormatter =
        DateTimeFormatter.ofPattern("hh:mm a", localeID)

    private val voucherDateFormatter =
        DateTimeFormatter.ofPattern("dd MMM yyyy", localeID)

    private val chatDateFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy", localeID)

    private val messageDateFormatter =
        DateTimeFormatter.ofPattern("MMM dd, yyyy", localeID)

    private val detailOrderDateFormatter =
        DateTimeFormatter.ofPattern("MMM dd, yyyy", localeID)


    fun formatChatTime(dateTime: LocalDateTime?): String {
        val messageTime = dateTime ?: LocalDateTime.now()

        val today = LocalDate.now()
        val messageDate = messageTime.toLocalDate()

        return if (messageDate == today) {
            messageTime.format(timeFormatter)
        } else {
            messageDate.format(chatDateFormatter)
        }
    }

    fun formatMessageTime(dateTime: LocalDateTime?): String {
        val messageTime = dateTime ?: LocalDateTime.now()
        return messageTime.format(timeFormatter)
    }

    fun formatMessageDate(dateTime: LocalDateTime?): String {
        val orderDate = dateTime?.toLocalDate()
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)

        val header = when (orderDate) {
            today -> "Today"
            yesterday -> "Yesterday"
            else -> orderDate?.format(messageDateFormatter)
        }
        return header?:""
    }

    fun utcToLocalDateTime(utc: String): LocalDateTime {
        return LocalDateTime.parse(utc)
            .atZone(ZoneOffset.UTC)
            .withZoneSameInstant(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    fun fromTimestamp(timestamp: Long): LocalDateTime {
        return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    fun fromDate(date: Date?): LocalDateTime {
        return fromTimestamp(date?.time?:0)
    }

    fun formatVoucherDate(dateTime: LocalDate?): String {
        val voucherTime = dateTime ?: LocalDate.now()
        return voucherTime.format(voucherDateFormatter)
    }

    fun formatOrderDate(dateTime: LocalDateTime?): String {
        val orderDateTime = dateTime ?: LocalDateTime.now()
        val orderDate = orderDateTime.toLocalDate()

        val today = LocalDate.now()
        val yesterday = today.minusDays(1)

        val header = when (orderDate) {
            today -> "Today, ${orderDateTime.format(timeFormatter)}"
            yesterday -> "Yesterday, ${orderDateTime.format(timeFormatter)}"
            else -> orderDate.format(detailOrderDateFormatter)
        }
        return header
    }

    fun getHeaderTimeAndDate(dateTime: LocalDateTime): DateTimeDisplay {
        val orderDate = dateTime.toLocalDate()
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)

        val header = when (orderDate) {
            today -> "Today"
            yesterday -> "Yesterday"
            else -> orderDate.format(messageDateFormatter)
        }

        val time = dateTime.format(timeFormatter)

        return DateTimeDisplay(
            header = header,
            time = time,
            localDate = orderDate
        )
    }
}