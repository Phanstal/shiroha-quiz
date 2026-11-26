package com.fufu.quiz.ui.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.ImportExport
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fufu.quiz.document.DocumentRecognitionManager
import com.fufu.quiz.state.QuizRepository
import com.fufu.quiz.ui.components.DocumentTaskCapsule
import com.fufu.quiz.ui.screens.AboutScreen
import com.fufu.quiz.ui.screens.AiSettingsScreen
import com.fufu.quiz.ui.screens.BankDetailScreen
import com.fufu.quiz.ui.screens.BankListScreen
import com.fufu.quiz.ui.screens.BankReviewScreen
import com.fufu.quiz.ui.screens.DataManagementScreen
import com.fufu.quiz.ui.screens.DocumentRecognitionScreen
import com.fufu.quiz.ui.screens.WebDavBackupScreen
import com.fufu.quiz.ui.components.WebDavTaskCapsule
import com.fufu.quiz.ui.screens.ExamScreen
import com.fufu.quiz.ui.screens.FavoriteScreen
import com.fufu.quiz.ui.screens.HomeScreen
import com.fufu.quiz.ui.screens.ImportScreen
import com.fufu.quiz.ui.screens.ImportSourceDraft
import com.fufu.quiz.ui.screens.MeScreen
import com.fufu.quiz.ui.screens.AppearancePreferenceScreen
import com.fufu.quiz.ui.screens.PracticePreferenceScreen
import com.fufu.quiz.ui.screens.PracticeQuickEditScreen
import com.fufu.quiz.ui.screens.QuestionSearchScreen
import com.fufu.quiz.ui.screens.PracticeScreen
import com.fufu.quiz.ui.screens.RecordDetailScreen
import com.fufu.quiz.ui.screens.RecordsScreen
import com.fufu.quiz.ui.screens.StandardImportFormatScreen
import com.fufu.quiz.ui.screens.WrongBookPreferenceScreen
import com.fufu.quiz.ui.screens.WrongBookScreen
import com.fufu.quiz.ui.theme.FufuColors
import com.fufu.quiz.ui.theme.FufuDimens
import com.fufu.quiz.ui.theme.FufuMotion
import com.fufu.quiz.ui.theme.FufuRadius

private val FufuPageEaseOut = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)

private enum class MainTab(
    val title: String,
    val icon: ImageVector,
    val showInBottomBar: Boolean = true
) {
    Home("首页", Icons.Rounded.Dashboard),
    Practice("练习", Icons.Rounded.School),
    Import("导入", Icons.Rounded.ImportExport),
    Me("我的", Icons.Rounded.Settings),
    Exam("考试", Icons.Rounded.School, showInBottomBar = false),
    BankList("题库管理", Icons.Rounded.Dashboard, showInBottomBar = false),
    QuestionSearch("题目搜索", Icons.Rounded.Search, showInBottomBar = false),
    BankDetail("题库详情", Icons.Rounded.Dashboard, showInBottomBar = false),
    BankReview("题库核对", Icons.Rounded.Dashboard, showInBottomBar = false),
    WrongBook("错题本", Icons.Rounded.School, showInBottomBar = false),
    Favorites("收藏夹", Icons.Rounded.School, showInBottomBar = false),
    Records("记录", Icons.Rounded.Dashboard, showInBottomBar = false),
    RecordDetail("记录详情", Icons.Rounded.Dashboard, showInBottomBar = false),
    AppearancePreference("外观偏好", Icons.Rounded.Settings, showInBottomBar = false),
    PracticePreference("刷题偏好", Icons.Rounded.School, showInBottomBar = false),
    PracticeQuickEdit("快速编辑", Icons.Rounded.School, showInBottomBar = false),
    WrongBookPreference("错题本设置", Icons.Rounded.School, showInBottomBar = false),
    AiSettings("AI 设置", Icons.Rounded.Settings, showInBottomBar = false),
    DocumentRecognition("文档识别", Icons.Rounded.ImportExport, showInBottomBar = false),
    DataManagement("数据管理", Icons.Rounded.Settings, showInBottomBar = false),
    CloudBackup("云端备份", Icons.Rounded.ImportExport, showInBottomBar = false),
    StandardFormat("标准格式", Icons.Rounded.ImportExport, showInBottomBar = false),
    About("关于", Icons.Rounded.Settings, showInBottomBar = false)
}

private fun MainTab.fallbackBackTarget(): MainTab? = when (this) {
    MainTab.Home,
    MainTab.Practice,
    MainTab.Import,
    MainTab.Me -> null

    MainTab.Exam,
    MainTab.BankList,
    MainTab.WrongBook,
    MainTab.Favorites,
    MainTab.Records -> MainTab.Home

    MainTab.QuestionSearch,
    MainTab.BankDetail -> MainTab.BankList
    MainTab.BankReview -> MainTab.BankDetail
    MainTab.RecordDetail -> MainTab.Records
    MainTab.PracticeQuickEdit -> MainTab.Practice
    MainTab.CloudBackup -> MainTab.DataManagement
    MainTab.AppearancePreference,
    MainTab.PracticePreference,
    MainTab.WrongBookPreference,
    MainTab.AiSettings,
    MainTab.DocumentRecognition,
    MainTab.DataManagement,
    MainTab.StandardFormat,
    MainTab.About -> MainTab.Me
}

private data class AppRouteSnapshot(
    val tab: MainTab,
    val bankId: String? = null,
    val recordId: String? = null
)

private const val ROUTE_PART_SEPARATOR = "\u001F"

private fun AppRouteSnapshot.encode(): String = listOf(
    tab.name,
    bankId.orEmpty(),
    recordId.orEmpty()
).joinToString(ROUTE_PART_SEPARATOR)

private fun decodeRouteSnapshot(value: String): AppRouteSnapshot? {
    val parts = value.split(ROUTE_PART_SEPARATOR, limit = 3)
    val tab = parts.firstOrNull()?.let { name ->
        MainTab.entries.firstOrNull { it.name == name }
    } ?: return null
    return AppRouteSnapshot(
        tab = tab,
        bankId = parts.getOrNull(1)?.takeIf { it.isNotEmpty() },
        recordId = parts.getOrNull(2)?.takeIf { it.isNotEmpty() }
    )
}

@Composable
fun FufuAppShell() {
    val importSourceDraft = remember { ImportSourceDraft() }
    var currentTab by rememberSaveable { mutableStateOf(MainTab.Home) }
    var detailBankId by rememberSaveable { mutableStateOf<String?>(null) }
    var detailRecordId by rememberSaveable { mutableStateOf<String?>(null) }
    var routeBackStack by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var selectedRootTab by rememberSaveable { mutableStateOf(MainTab.Home) }

    fun currentRouteSnapshot() = AppRouteSnapshot(
        tab = currentTab,
        bankId = detailBankId,
        recordId = detailRecordId
    )

    fun navigateTo(
        target: MainTab,
        bankId: String? = null,
        recordId: String? = null
    ) {
        val targetBankId = bankId ?: detailBankId
        val targetRecordId = recordId ?: detailRecordId
        if (
            currentTab == target &&
            detailBankId == targetBankId &&
            detailRecordId == targetRecordId
        ) {
            return
        }
        routeBackStack = routeBackStack + currentRouteSnapshot().encode()
        detailBankId = targetBankId
        detailRecordId = targetRecordId
        currentTab = target
    }

    fun navigateRoot(target: MainTab) {
        routeBackStack = emptyList()
        currentTab = target
        if (target.showInBottomBar) {
            selectedRootTab = target
        }
    }

    fun navigateBack() {
        val previous = routeBackStack.lastOrNull()?.let(::decodeRouteSnapshot)
        if (previous != null) {
            routeBackStack = routeBackStack.dropLast(1)
            if (previous.tab == MainTab.BankDetail || previous.tab == MainTab.BankReview) {
                detailBankId = previous.bankId
            }
            if (previous.tab == MainTab.RecordDetail) {
                detailRecordId = previous.recordId
            }
            currentTab = previous.tab
            return
        }
        currentTab.fallbackBackTarget()?.let { fallback ->
            currentTab = fallback
        }
    }

    val hasBackTarget = routeBackStack.isNotEmpty() || currentTab.fallbackBackTarget() != null
    BackHandler(enabled = hasBackTarget) {
        navigateBack()
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useSideNavigation = QuizRepository.tabletSideNavigationEnabled &&
            maxWidth >= 600.dp &&
            maxHeight >= 480.dp

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (!useSideNavigation) {
                    Column {
                        val documentTask = DocumentRecognitionManager.task
                        if (
                            currentTab != MainTab.DocumentRecognition &&
                            documentTask != null &&
                            DocumentRecognitionManager.shouldShowGlobalTaskCapsule
                        ) {
                            DocumentTaskCapsule(
                                task = documentTask,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                onClick = { navigateTo(MainTab.DocumentRecognition) }
                            )
                        }
                        WebDavTaskCapsule(visible = currentTab != MainTab.CloudBackup,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            onOpen = { navigateTo(MainTab.CloudBackup) })
                        BottomAppBar(
                            containerColor = FufuColors.BottomBar,
                            tonalElevation = 0.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = FufuDimens.BottomBarHorizontalPadding,
                                        vertical = FufuDimens.BottomBarVerticalPadding
                                    ),
                                horizontalArrangement = Arrangement.spacedBy(FufuDimens.BottomNavItemGap),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                MainTab.entries.filter { it.showInBottomBar }.forEach { tab ->
                                    FufuBottomNavItem(
                                        tab = tab,
                                        selected = selectedRootTab == tab,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            if (currentTab != tab) navigateRoot(tab)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useSideNavigation) {
                    FufuSideNavigation(
                        selectedTab = selectedRootTab,
                        onTabClick = { tab ->
                            if (currentTab != tab) navigateRoot(tab)
                        }
                    )
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    FufuColors.BgGradientTop,
                                    FufuColors.BgGradientMiddle,
                                    FufuColors.BgGradientBottom
                                )
                            )
                        )
                ) {
                    AnimatedContent(
                    targetState = currentTab,
                    modifier = Modifier.fillMaxSize(),
                    transitionSpec = {
                        (fadeIn(
                            animationSpec = tween(durationMillis = FufuMotion.PageTransitionMillis, easing = FufuPageEaseOut)
                        ) + slideInVertically(
                            animationSpec = tween(durationMillis = FufuMotion.PageTransitionMillis, easing = FufuPageEaseOut),
                            initialOffsetY = { FufuMotion.PageTransitionOffsetPx }
                        )) togetherWith fadeOut(
                            animationSpec = tween(durationMillis = FufuMotion.PageFadeOutMillis)
                        )
                    },
                    contentAlignment = Alignment.TopStart,
                    label = "main_tabs"
                ) { tab ->
                    when (tab) {
                        MainTab.Home -> HomeScreen(
                            onGoImport = { navigateRoot(MainTab.Import) },
                            onGoPractice = { navigateRoot(MainTab.Practice) },
                            onGoExam = { navigateTo(MainTab.Exam) },
                            onOpenBankList = { navigateTo(MainTab.BankList) },
                            onOpenBankDetail = { bankId ->
                                navigateTo(MainTab.BankDetail, bankId = bankId)
                            },
                            onOpenWrongBook = { navigateTo(MainTab.WrongBook) },
                            onOpenFavorites = { navigateTo(MainTab.Favorites) },
                            onOpenRecords = { navigateTo(MainTab.Records) }
                        )
    
                        MainTab.Practice -> PracticeScreen(
                            onGoExam = { navigateTo(MainTab.Exam) },
                            onOpenRecords = { navigateTo(MainTab.Records) },
                            onOpenQuickEdit = { navigateTo(MainTab.PracticeQuickEdit) }
                        )
                        MainTab.PracticeQuickEdit -> PracticeQuickEditScreen(
                            onBack = { navigateBack() }
                        )
                        MainTab.Import -> ImportScreen(
                            sourceDraft = importSourceDraft,
                            onImportSaved = {
                                importSourceDraft.reset()
                                navigateRoot(MainTab.Home)
                            },
                            onOpenPreference = { navigateTo(MainTab.AiSettings) },
                            onOpenDocumentRecognition = { navigateTo(MainTab.DocumentRecognition) },
                            ocrImportDraft = DocumentRecognitionManager.pendingImportDraft,
                            onOcrImportDraftConsumed = DocumentRecognitionManager::consumePendingImportDraft
                        )
                        MainTab.Me -> MeScreen(
                            onOpenRecords = { navigateTo(MainTab.Records) },
                            onOpenAppearancePreference = { navigateTo(MainTab.AppearancePreference) },
                            onOpenPracticePreference = { navigateTo(MainTab.PracticePreference) },
                            onOpenWrongBookPreference = { navigateTo(MainTab.WrongBookPreference) },
                            onOpenAiSettings = { navigateTo(MainTab.AiSettings) },
                            onOpenDataManagement = { navigateTo(MainTab.DataManagement) },
                            onOpenStandardFormat = { navigateTo(MainTab.StandardFormat) },
                            onOpenAbout = { navigateTo(MainTab.About) }
                        )
                        MainTab.Exam -> ExamScreen(
                            onBackHome = { navigateRoot(MainTab.Home) },
                            onGoPractice = { navigateRoot(MainTab.Practice) },
                            onOpenRecord = { recordId ->
                                navigateTo(MainTab.RecordDetail, recordId = recordId)
                            }
                        )
                        MainTab.BankList -> BankListScreen(
                            onBack = { navigateBack() },
                            onOpenQuestionSearch = { navigateTo(MainTab.QuestionSearch) },
                            onOpenBankDetail = { bankId ->
                                navigateTo(MainTab.BankDetail, bankId = bankId)
                            }
                        )
                        MainTab.QuestionSearch -> QuestionSearchScreen(
                            onBack = { navigateBack() },
                            onOpenBankDetail = { bankId ->
                                navigateTo(MainTab.BankDetail, bankId = bankId)
                            }
                        )
                        MainTab.BankDetail -> BankDetailScreen(
                            bankId = detailBankId,
                            onBack = { navigateBack() },
                            onGoPractice = { navigateRoot(MainTab.Practice) },
                            onGoExam = { navigateTo(MainTab.Exam) },
                            onOpenReview = { navigateTo(MainTab.BankReview) }
                        )
                        MainTab.BankReview -> BankReviewScreen(
                            bankId = detailBankId,
                            onBack = { navigateBack() }
                        )
                        MainTab.WrongBook -> WrongBookScreen(
                            onBack = { navigateBack() },
                            onGoPractice = { navigateRoot(MainTab.Practice) }
                        )
                        MainTab.Favorites -> FavoriteScreen(
                            onBack = { navigateBack() },
                            onGoPractice = { navigateRoot(MainTab.Practice) }
                        )
                        MainTab.Records -> RecordsScreen(
                            onBack = { navigateBack() },
                            onOpenRecord = { recordId ->
                                navigateTo(MainTab.RecordDetail, recordId = recordId)
                            }
                        )
                        MainTab.RecordDetail -> RecordDetailScreen(
                            recordId = detailRecordId,
                            onBack = { navigateBack() }
                        )
                        MainTab.AppearancePreference -> AppearancePreferenceScreen(
                            onBack = { navigateBack() }
                        )
                        MainTab.PracticePreference -> PracticePreferenceScreen(
                            onBack = { navigateBack() }
                        )
                        MainTab.WrongBookPreference -> WrongBookPreferenceScreen(
                            onBack = { navigateBack() }
                        )
                        MainTab.AiSettings -> AiSettingsScreen(
                            onBack = { navigateBack() }
                        )
                        MainTab.DocumentRecognition -> DocumentRecognitionScreen(
                            onBack = { navigateBack() },
                            onUseResultForImport = { navigateRoot(MainTab.Import) }
                        )
                        MainTab.DataManagement -> DataManagementScreen(
                            onBack = { navigateBack() },
                            onOpenCloudBackup = { navigateTo(MainTab.CloudBackup) }
                        )
                        MainTab.CloudBackup -> WebDavBackupScreen(onBack = { navigateBack() })
                        MainTab.StandardFormat -> StandardImportFormatScreen(
                            onBack = { navigateBack() }
                        )
                        MainTab.About -> AboutScreen(
                            onBack = { navigateBack() }
                        )
                    }
                }
                    if (useSideNavigation) Column(modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 24.dp, vertical = 16.dp)) {
                        val documentTask = DocumentRecognitionManager.task
                        if (currentTab != MainTab.DocumentRecognition && documentTask != null && DocumentRecognitionManager.shouldShowGlobalTaskCapsule) {
                        DocumentTaskCapsule(
                            task = documentTask,
                            onClick = { navigateTo(MainTab.DocumentRecognition) }
                        )
                        }
                        WebDavTaskCapsule(visible = currentTab != MainTab.CloudBackup,
                            onOpen = { navigateTo(MainTab.CloudBackup) })
                    }
            }
        }
    }
}
}

@Composable
private fun FufuSideNavigation(
    selectedTab: MainTab,
    onTabClick: (MainTab) -> Unit
) {
    NavigationRail(
        modifier = Modifier
            .width(88.dp)
            .fillMaxHeight(),
        containerColor = FufuColors.BottomBar
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        MainTab.entries.filter { it.showInBottomBar }.forEach { tab ->
            val selected = selectedTab == tab
            NavigationRailItem(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                selected = selected,
                onClick = { onTabClick(tab) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = null,
                        modifier = Modifier.size(FufuDimens.BottomNavIconSize)
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                    unselectedIconColor = FufuColors.TextSecondary,
                    unselectedTextColor = FufuColors.TextSecondary
                )
            )
        }
    }
}

@Composable
private fun FufuBottomNavItem(
    tab: MainTab,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.10f) else Color.Transparent,
        animationSpec = tween(durationMillis = FufuMotion.BottomNavMillis),
        label = "bottom_nav_bg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else FufuColors.TextSecondary,
        animationSpec = tween(durationMillis = FufuMotion.BottomNavMillis),
        label = "bottom_nav_color"
    )
    val iconScale by animateFloatAsState(
        targetValue = if (selected) FufuDimens.BottomNavIconSelectedScale else 1f,
        animationSpec = tween(durationMillis = FufuMotion.BottomNavMillis),
        label = "bottom_nav_icon_scale"
    )
    val shape = RoundedCornerShape(FufuRadius.Md)

    Surface(
        modifier = modifier
            .height(FufuDimens.BottomNavItemHeight)
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = shape,
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = FufuDimens.BottomNavItemHorizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier
                    .size(FufuDimens.BottomNavIconSize)
                    .graphicsLayer {
                        scaleX = iconScale
                        scaleY = iconScale
                    }
            )
            Text(
                text = tab.title,
                color = contentColor,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
