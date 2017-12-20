package org.hisp.dhis.android.core.category;


import static org.hisp.dhis.android.core.utils.Utils.isDeleted;

import android.support.annotation.NonNull;

import java.util.List;

public class CategoryHandler {

    private final CategoryOptionHandler categoryOptionHandler;
    private final CategoryOptionLinkStore categoryOptionLinkStore;
    private final CategoryStore categoryStore;

    public CategoryHandler(
            @NonNull CategoryStore categoryStore,
            @NonNull CategoryOptionHandler categoryOptionHandler,
            @NonNull CategoryOptionLinkStore categoryOptionLinkStore) {
        this.categoryStore = categoryStore;
        this.categoryOptionHandler = categoryOptionHandler;
        this.categoryOptionLinkStore = categoryOptionLinkStore;
    }

    public void handle(Category category) {

        if (isDeleted(category)) {
            categoryStore.delete(category);
        } else {

            boolean updated = categoryStore.update(category, category);

            if (!updated) {
                categoryStore.insert(category);
                handleCategoryOption(category);
            }
        }
    }

    private void handleCategoryOption(@NonNull Category category) {
        List<CategoryOption> categoryOptions = category.categoryOptions();
        if (categoryOptions != null) {

            for (CategoryOption option : categoryOptions) {
                categoryOptionHandler.handle(option);

                CategoryOptionLinkModel link = newCategoryOption(category, option);

                categoryOptionLinkStore.insert(link);
            }
        }
    }

    private CategoryOptionLinkModel newCategoryOption(@NonNull Category category,
            @NonNull CategoryOption option) {

        return CategoryOptionLinkModel.builder().category(
                category.uid())
                .option(option.uid())
                .build();
    }
}
