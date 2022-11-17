from typing import List

from apitable.types import MetaView


class ViewManager:

    def __init__(self, dst: 'Datasheet'):
        self.dst = dst

    def all(self) -> List[MetaView]:
        """
        Query all views of the current Vig table
        @return: List[MetaView]
        """
        return self.dst.get_views()
