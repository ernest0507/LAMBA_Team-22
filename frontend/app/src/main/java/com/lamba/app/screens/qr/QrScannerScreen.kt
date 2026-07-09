package com.lamba.app.screens.qr

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import components.BackButton

@Composable
fun QrScannerScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val scanner = remember {
        GmsBarcodeScanning.getClient(context)
    }

    var scannedValue by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column {
        BackButton(onClick = onBackClick)

        Text("QR сканер")

        Button(
            onClick = {
                scanner.startScan()
                    .addOnSuccessListener { barcode ->
                        scannedValue = barcode.rawValue
                        errorMessage = null
                    }
                    .addOnCanceledListener {
                        errorMessage = "Сканирование отменено"
                    }
                    .addOnFailureListener { error ->
                        errorMessage = "Сканирование недоступно :/"
                    }
            }
        ) {
            Text("Сканировать QR-код")
        }
        scannedValue?.let {
            Text(it)
        }
        errorMessage?.let {
            Text(it)
        }
    }
}


