package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Orange600
import org.threeten.bp.LocalDate

@Composable
fun DayItem(
    day: Int,
    isSelectedStart: Boolean,
    isSelectedEnd: Boolean,
    isInRange: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .clickable{ onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Background untuk rentang (Tanggal 20, 21, 22)
        if (isInRange) {
            Box(modifier = Modifier.fillMaxSize().background(Orange50))
        }

        // Lingkaran untuk Start/End (Tanggal 19 & 23)
        if (isSelectedStart || isSelectedEnd) {
            Box(modifier = Modifier.fillMaxSize().background(Orange500, CircleShape))
        }

        Text(
            text = day.toString(),
            style = when {
                isSelectedStart || isSelectedEnd || isInRange -> LocalCustomTypography.current.h5Bold
                else -> LocalCustomTypography.current.h5Regular
            },
            color = when {
                isSelectedStart || isSelectedEnd -> LocalCustomColors.current.background
                isInRange -> Orange500
                else -> LocalCustomColors.current.text
            }
        )
    }
}

@Composable
fun DateGrid(
    currentMonth: LocalDate,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDateClick: (LocalDate) -> Unit
) {
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.withDayOfMonth(1).dayOfWeek.value % 7

    DaysOfWeekHeader()
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        userScrollEnabled = false
    ) {
        items(firstDayOfMonth) { Spacer(modifier = Modifier.fillMaxSize()) }
        items(daysInMonth) { index ->
            val date = currentMonth.withDayOfMonth(index + 1)
            DayItem(
                day = index + 1,
                isSelectedStart = date == startDate,
                isSelectedEnd = date == endDate,
                isInRange = startDate != null && endDate != null &&
                        date.isAfter(startDate) && date.isBefore(endDate),
                onClick = { onDateClick(date) }
            )
        }
    }
}

@Composable
fun CalendarHeader(
    currentMonth: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    // Format month name (ex: "June 2023")
    val monthName = currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val year = currentMonth.year

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: "June 2023"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {}
        ) {
            Text(
                text = "$monthName $year",
                style = LocalCustomTypography.current.bodyMediumBold,
                color = LocalCustomColors.current.headerText
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = LocalCustomColors.current.headerText,
                modifier = Modifier.size(24.dp)
            )
        }

        Row {
            IconButton(onClick = onPreviousMonth) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Prev",
                    tint = Orange600
                )
            }
            IconButton(onClick = onNextMonth) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next",
                    tint = Orange600
                )
            }
        }
    }
}

@Composable
fun DaysOfWeekHeader() {
    val days = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
    Row(modifier = Modifier.fillMaxWidth()) {
        days.forEach { day ->
            Text(
                modifier = Modifier.weight(1f),
                text = day,
                style = LocalCustomTypography.current.bodySmallSemiBold,
                color = LocalCustomColors.current.text.copy(alpha = 0.4f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DatePicker (
    currentMonth: LocalDate,
    startDate : LocalDate?,
    endDate: LocalDate?,
    onMonthChange: (LocalDate) -> Unit,
    onDateClick: (LocalDate) -> Unit
){
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CalendarHeader(
            currentMonth = currentMonth,
            onNextMonth = { onMonthChange(currentMonth.plusMonths(1)) },
            onPreviousMonth = { onMonthChange(currentMonth.minusMonths(1)) }
        )
        DateGrid(
            currentMonth = currentMonth,
            startDate = startDate,
            endDate = endDate,
            onDateClick = onDateClick
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DateGridPreview() {
//    val mockStartDate = LocalDate.of(2026,10,13)
//    val mockEndDate = null
//
//    DatePicker( currentMonth = mockStartDate, startDate = mockStartDate,mockEndDate, onMonthChange = {}, onDateClick = {})
//}