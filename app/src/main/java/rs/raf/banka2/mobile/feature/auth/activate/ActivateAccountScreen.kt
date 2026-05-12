package rs.raf.banka2.mobile.feature.auth.activate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import rs.raf.banka2.mobile.core.ui.components.AnimatedBackground
import rs.raf.banka2.mobile.core.ui.components.AppTextField
import rs.raf.banka2.mobile.core.ui.components.BankaScaffold
import rs.raf.banka2.mobile.core.ui.components.ErrorBanner
import rs.raf.banka2.mobile.core.ui.components.GlassCard
import rs.raf.banka2.mobile.core.ui.components.PrimaryButton

@Composable
fun ActivateAccountScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: ActivateAccountViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is ActivateEvent.Success) onSuccess()
        }
    }

    BankaScaffold(
        title = "Aktivacija naloga",
        onBack = onBack,
        backgroundDecoration = {
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                AnimatedBackground()
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                // Spec Sc 9 + ad-hoc bug 12.05.2026: pre renderovanja forme,
                // proverava stanje tokena. Distinkni ekrani za svaki status.
                when (state.tokenStatus) {
                    TokenStatus.CHECKING -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Provera linka za aktivaciju...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    TokenStatus.USED -> {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Link je vec iskoriscen",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Aktivacioni link je vec iskoriscen. " +
                                    (state.tokenEmail?.let { "Mozete se prijaviti sa: $it" }
                                        ?: "Mozete se odmah prijaviti."),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(20.dp))
                        PrimaryButton(
                            text = "Idi na prijavu",
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    TokenStatus.ALREADY_ACTIVE -> {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Nalog je vec aktivan",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = state.tokenEmail?.let { "Prijavite se sa email-om: $it" }
                                ?: "Vas nalog je vec aktiviran. Prijavite se sa email-om i lozinkom.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(20.dp))
                        PrimaryButton(
                            text = "Idi na prijavu",
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    TokenStatus.EXPIRED -> {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Link za aktivaciju je istekao",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Aktivacioni link traje 24 sata. Kontaktirajte administratora da vam posalje novi link.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(20.dp))
                        PrimaryButton(
                            text = "Nazad na prijavu",
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    TokenStatus.INVALID -> {
                        Icon(
                            imageVector = Icons.Filled.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Nevazeci link za aktivaciju",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Aktivacioni token ne postoji u sistemu. Kontaktirajte administratora za novi link.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(20.dp))
                        PrimaryButton(
                            text = "Nazad na prijavu",
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    TokenStatus.VALID -> {
                        Text(
                            text = "Aktiviraj svoj nalog zaposlenog",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = state.tokenEmail?.let { "Aktivira se nalog za: $it" }
                                ?: "Unesi token koji je banka poslala na tvoj email i postavi inicijalnu lozinku.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(20.dp))
                        AppTextField(
                            value = state.token,
                            onValueChange = viewModel::onTokenChange,
                            label = "Aktivacioni token",
                            leadingIcon = Icons.Outlined.VpnKey,
                            imeAction = ImeAction.Next,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        AppTextField(
                            value = state.password,
                            onValueChange = viewModel::onPasswordChange,
                            label = "Lozinka",
                            leadingIcon = Icons.Outlined.Lock,
                            isPassword = true,
                            imeAction = ImeAction.Next,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        AppTextField(
                            value = state.confirm,
                            onValueChange = viewModel::onConfirmChange,
                            label = "Potvrdi lozinku",
                            leadingIcon = Icons.Outlined.Lock,
                            isPassword = true,
                            imeAction = ImeAction.Done,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        ErrorBanner(message = state.error)
                        Spacer(Modifier.height(16.dp))
                        PrimaryButton(
                            text = "Aktiviraj nalog",
                            onClick = viewModel::submit,
                            loading = state.isSubmitting,
                            leadingIcon = Icons.Filled.PersonAdd,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
