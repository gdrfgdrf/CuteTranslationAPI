package io.github.gdrfgdrf.cutetranslationapi.utils

import com.google.protobuf.Message
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class Protobuf<T : Message> {
    var message: T? = null
    var storeFile: File? = null

    fun save() {
        if (message == null ||
            storeFile == null ||
            !storeFile!!.exists() ||
            !storeFile!!.isFile) {
            return
        }

        val fileOutputStream = FileOutputStream(storeFile!!)
        val buffOutputStream = BufferedOutputStream(fileOutputStream)
        val bytes = message?.toByteArray()
        bytes?.let {
            buffOutputStream.write(bytes)
            buffOutputStream.close()
            fileOutputStream.close()
        }
    }

    companion object {
        fun <T : Message> get(
            storeFile: File,
            parse: (ByteArray) -> T,
        ): Protobuf<T>? {
            if (!storeFile.exists() || !storeFile.isFile) {
                return null
            }

            val fileInputStream = FileInputStream(storeFile)
            val bufferedInputStream = BufferedInputStream(fileInputStream)
            val bytes = bufferedInputStream.readBytes()

            fileInputStream.close()
            bufferedInputStream.close()

            val t = parse(bytes)

            val protobuf = Protobuf<T>()
            protobuf.message = t
            protobuf.storeFile = storeFile

            return protobuf
        }
    }
}