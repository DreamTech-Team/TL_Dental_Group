interface ListCategory1 {
  id: string;
  title: string;
  img: string;
  highlight: number;
  slug: string;
}

interface ListCategory2 {
  id: string;
  title: string;
  slug: string;
}

interface DataRender {
  title: string;
  slug: string;
  data: { name: string; slug: string }[];
}

const coverDataCategory = {
  covertBase64ToBlob(
    listCategory1: ListCategory1[],
    listCategory2: ListCategory2[],
    dataRender: DataRender[]
  ): DataRender[] {
    const uniqueElementsSet = new Set<string>();
    const duplicatesPositions: { [key: string]: number[] } = {};
    const duplicatesPositions1: { [key: string]: number[] } = {};

    listCategory1.forEach((item, index) => {
      const { id } = item;

      if (uniqueElementsSet.has(id)) {
        if (!duplicatesPositions[id]) {
          duplicatesPositions[id] = [index];
        } else {
          duplicatesPositions[id].push(index);
        }
      } else {
        uniqueElementsSet.add(id);
        duplicatesPositions1[id] = [index];
        duplicatesPositions1[id].push(index);
      }
    });

    Object.keys(duplicatesPositions).forEach((id) => {
      duplicatesPositions[id].push(duplicatesPositions1[id][0]);
      delete duplicatesPositions1[id];
    });

    const uniqueElementsArray = Array.from(uniqueElementsSet).map((id) => {
      return listCategory1.find((item) => item.id === id);
    });

    uniqueElementsArray.forEach((item) => {
      if (item) {
        const object = {
          title: '',
          slug: '',
          data: [
            {
              name: '',
              slug: ''
            }
          ]
        };
        const dataCate2: { name: string; slug: string }[] = [];

        if (duplicatesPositions1[item.id]) {
          object.title = item.title;
          object.slug = item.slug;

          if (listCategory2[duplicatesPositions1[item.id][0]] !== null) {
            dataCate2.push({
              name: listCategory2[duplicatesPositions1[item.id][0]].title,
              slug: listCategory2[duplicatesPositions1[item.id][0]].slug
            });
          }

          object.data = dataCate2;

          dataRender.push(object);
        }
        if (duplicatesPositions[item.id]) {
          object.title = item.title;
          object.slug = item.slug;

          duplicatesPositions[item.id].forEach((replace) => {
            if (listCategory2[replace] !== null) {
              dataCate2.push({
                name: listCategory2[replace].title,
                slug: listCategory2[replace].slug
              });
            }
          });

          object.data = dataCate2;

          dataRender.push(object);
        }
      }
    });

    return dataRender;
  }
};

export default coverDataCategory;
