import { ref } from 'vue';
import { defineStore } from 'pinia';

interface Dictionaries {
  slug: string;
  name: string;
}

export const saveBreadcrumbs = defineStore('saveBreadcrumbs', () => {
  const breadcrumbs = ref<Dictionaries[]>([]);

  const setBreadcrumbs = (newActiveArray: Dictionaries[]) => {
    newActiveArray.forEach((newActive) => {
      const exists = breadcrumbs.value.some((crumb) => crumb.slug === newActive.slug);
      if (!exists) {
        breadcrumbs.value.push({ ...newActive });
      }
    });
  };

  return { breadcrumbs, setBreadcrumbs };
});

export const saveSlugCates = defineStore('saveCates', () => {
  const cates = ref<Dictionaries[]>([]);

  const setSlugCates = (newActiveArray: Dictionaries[]) => {
    newActiveArray.forEach((newActive) => {
      const exists = cates.value.some((crumb) => crumb.slug === newActive.slug);
      if (!exists) {
        cates.value.push({ ...newActive });
      }
    });
  };

  return { cates, setSlugCates };
});

export const saveSlugNews = defineStore('saveNews', () => {
  const news = ref<Dictionaries[]>([]);

  const setSlugNews = (newActiveArray: Dictionaries[]) => {
    newActiveArray.forEach((newActive) => {
      const exists = news.value.some((crumb) => crumb.slug === newActive.slug);
      if (!exists) {
        news.value.push({ ...newActive });
      }
    });
  };

  return { news, setSlugNews };
});
