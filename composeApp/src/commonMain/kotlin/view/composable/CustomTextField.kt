package view.composable

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit, textStyle: TextStyle){
    TextField( modifier = modifier.height(80.dp),
        singleLine = true,
        maxLines = 1,
        value = value,
        onValueChange = {onValueChange(it)},
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = Color(0xFF8F00FF)
        ),
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    )
}