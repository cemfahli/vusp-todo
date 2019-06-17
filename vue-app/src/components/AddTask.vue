<template>
  <div class="d-flex flex-column align-items-center">
    <div class="input-group input-group col-md-5 mt-2">
      <input type="text"
        name="new-task"
        id="new-task"
        class="form-control mr-md-1 mr-1"
        v-model="newTask"
        @keyup.enter="addTask">
      <div class="input-group-append ">
        <button class="btn btn-primary"
          @click.prevent="addTask">
          <plus-icon stroke-width="4"></plus-icon>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import plus from './icons/BytesizePlus.vue';

export default {
  data() {
    return {
      newTask: '',
    };
  },
  components: {
    'plus-icon': plus,
  },
  methods: {
    addTask() {
      const siteUrl = document.URL;

      fetch(new URL('../items', siteUrl), {
        method: 'POST',
        mode: 'cors',
        headers: {
          Authorization: `Bearer ${this.$store.getters.getToken}`,
/*           'Content-Type': 'application/json', */
        },
        body: this.newTask,
      })
        .then((response) => {
          if (response !== null) {
            return response.json();
          }
          throw new Error("Cannot access server");
        })
        .then((data) => {
          if(data.error !== undefined) throw new Error(data.message);
          this.$store.commit('addTask', data);
          this.$store.commit('clearWarning');
          this.newTask = '';
        })
        .catch((error) => {
          console.log(error.message);
          this.$store.commit('setWarning',
            'Could not add task: ' + error.message);
        });
    },
  },
};
</script>
