<template>
  <div class="d-flex flex-column align-items-center">
    <ul class="list-group px-3 col-md-5">
      <li v-for="(task, index) in todoList" :key="task.id"
        class="list-group-item flex-column pr-2">
        <div class="task d-flex justify-content-between">
          {{ task.task }}
          <a href="#"
            class="badge badge-pill badge-danger my-auto pt-1"
            @click.prevent="removeTask(task.id, index)">
            <minus-icon stroke-width="4"></minus-icon>
          </a>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import minus from './icons/BytesizeMinus.vue';

export default {
  components: {
    'minus-icon': minus,
  },
  data() {
    return {
    };
  },
  computed: {
    todoList() {
      return this.$store.getters.getTasks;
    },
  },
  methods: {
    removeTask(id, index) {
      const siteUrl = document.URL;

      fetch(new URL(`../items/${id}`, siteUrl), {
        method: 'DELETE',
        mode: 'cors',
        headers: {
          Authorization: `Bearer ${this.$store.getters.getToken}`,
        },
      })
        .then((response) => {
          if (response.ok) {
            this.$store.commit('removeTask', index);
          } else {
            this.$store.commit('setWarning', 'Could not delete task');
          }
        });
    },
  },
  created() {
    const siteUrl = document.URL;

    fetch(new URL('../items', siteUrl), {
      method: 'GET',
      mode: 'cors',
      headers: {
        Authorization: `Bearer ${this.$store.getters.getToken}`,
      },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }
        throw new Error("Cannot access server");
      })
      .then((data) => {
        if(data.error !== undefined) throw new Error(data.message);
        this.$store.commit('addAllTasks', data);
        this.$store.commit('clearWarning');
      })
      .catch((error) => {
        console.log(error);
        this.$store.commit('setWarning',
          `Could not log in: ${error}`);
      });
  },
};
</script>

<style>
li {
  white-space: normal;
}
.task {
  word-wrap: break-word;
  word-break: break-all;
}
</style>
