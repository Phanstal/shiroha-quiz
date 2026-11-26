package com.fufu.quiz.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fufu.quiz.document.DocumentRecognitionTask
import com.fufu.quiz.document.DocumentTaskStage
import com.fufu.quiz.document.isWorking
import com.fufu.quiz.ui.theme.FufuColors
import com.fufu.quiz.ui.theme.FufuDimens
import com.fufu.quiz.ui.theme.FufuRadius

@Composable
fun DocumentTaskCapsule(
    task: DocumentRecognitionTask,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fufuNoRippleClickable(onClick = onClick),
        shape = RoundedCornerShape(FufuRadius.Pill),
        color = FufuColors.CardWhite86,
        border = BorderStroke(FufuDimens.Hairline, FufuColors.LineSelected),
        shadowElevation = 5.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (task.isWorking) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.5.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(
                    imageVector = when (task.stage) {
                        DocumentTaskStage.READY -> Icons.Rounded.CheckCircle
                        DocumentTaskStage.PAUSED -> Icons.Rounded.PauseCircle
                        DocumentTaskStage.FAILED -> Icons.Rounded.ErrorOutline
                        else -> Icons.Rounded.CloudUpload
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when (task.stage) {
                        DocumentTaskStage.READY -> "PDF 识别完成"
                        DocumentTaskStage.PAUSED -> "PDF 识别已暂停"
                        DocumentTaskStage.FAILED -> "PDF 识别需要处理"
                        else -> "正在在线识别 PDF"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = task.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "查看",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
