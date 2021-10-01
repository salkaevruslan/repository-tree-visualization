package components

import kotlinx.browser.document
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.attrs
import react.dom.form
import react.dom.input

external interface InputFieldProps : Props {
    var repo: String
    var branch: String
    var onUpdateRepo: (String) -> Unit
    var onUpdateBranch: (String) -> Unit
    var onSubmitValues: () -> Unit
}

val InputField = fc<InputFieldProps> { props ->
    form {
        attrs {
            onSubmitFunction = { event ->
                event.preventDefault()
                console.log(props.repo)
                props.onSubmitValues()
                (document.getElementById("repoNameField") as HTMLInputElement).value = ""
                (document.getElementById("branchNameField") as HTMLInputElement).value = ""
            }
        }
        input(type = InputType.text, name = "Reponame") {
            attrs {
                id = "repoNameField"
                placeholder = "Enter Repo Name"
                onChangeFunction = { event ->
                    event.preventDefault()
                    props.onUpdateRepo((event.target as HTMLInputElement).value)
                }
            }
        }
        input(type = InputType.text, name = "BranchName") {
            attrs {
                id = "branchNameField"
                placeholder = "Enter Branch Name"
                onChangeFunction = { event ->
                    event.preventDefault()
                    props.onUpdateBranch((event.target as HTMLInputElement).value)
                }
            }
        }
        input(type = InputType.submit, name = "Submit") {
            attrs {
                id = "sendButton"
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