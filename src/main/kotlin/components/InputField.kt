package components

import kotlinx.browser.window
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.attrs
import react.dom.form
import react.dom.input

external interface InputFieldProps : Props {
    var value: String
    var onUpdateValue: (String) -> Unit
    var onSubmitValue: () -> Unit
}

val InputField = fc<InputFieldProps> { props ->
    form {
        attrs {
            onSubmitFunction = { event ->
                event.preventDefault()
                props.onSubmitValue()
            }
            onChangeFunction = { event ->
                event.preventDefault()
                props.onUpdateValue((event.target as HTMLInputElement).value)
            }
        }
        input(type = InputType.text, name = "Reponame") {
            key = "repoNameField"
            attrs {
                placeholder = "Enter Repo Name"
            }
        }
        input(type = InputType.submit, name = "Submit") {
            key = "sendButton"
            attrs {
                placeholder = "Submit"
            }
        }
    }
}

fun RBuilder.inputField(handler: InputFieldProps.() -> Unit) = child(InputField) {
    attrs {
        handler()
    }
}