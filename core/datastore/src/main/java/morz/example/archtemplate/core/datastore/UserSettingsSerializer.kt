package morz.example.archtemplate.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import morz.example.archtemplate.UserSettings
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserSettingsSerializer @Inject constructor() : Serializer<UserSettings> {
    override val defaultValue: UserSettings
        get() = UserSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserSettings {
        try {
            return UserSettings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        t.writeTo(output)
    }
}
