package com.example.beachprofile.sessions

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import com.example.beachprofile.measures.Measure
import kotlinx.coroutines.CoroutineScope
import java.time.format.DateTimeFormatter

val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

fun convertMeasuresToCSV(measures: List<Measure>, session: Session): String {
    val header = "id,sessionName,sessionTimestamp,inclination,longitude,latitude,note,timestamp"
    val rows = measures.joinToString("\n") { m ->
        "${m.id},${session.name},${session.date},${m.inclination},${m.longitude},${m.latitude}," +
                "\"${m.note.replace("\"", "\"\"")}\",${m.timestamp}"
    }
    return "$header\n$rows"
}

fun saveCSVToUri(context: Context, uri: Uri, csvData: String) {
    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
        outputStream.write(csvData.toByteArray())
        outputStream.flush()
    }
}

fun CoroutineScope.batchExportCSVsToFolder(
    context: Context,
    folderUri: Uri,
    sessionWithMeasures: List<SessionWithMeasures>
) {
    val contentResolver = context.contentResolver
    val pickedDir = DocumentFile.fromTreeUri(context, folderUri)

    for (sm in sessionWithMeasures) {
        val csv = convertMeasuresToCSV(sm.measures, sm.session)
        val filename = "session_${sm.session.name.replace(" ", "_")}.csv"
        val newFile = pickedDir!!.createFile("text/csv", filename)

        newFile?.let {
            context.contentResolver.openOutputStream(it.uri)?.use { outputStream ->
                outputStream.write(csv.toByteArray())
                outputStream.flush()
            }
        }
    }
}